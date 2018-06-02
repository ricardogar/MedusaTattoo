(function () {
    'use strict';

    angular
        .module('medusaTattooApp')
        .config(['calendarConfig', function (calendarConfig) {
            calendarConfig.dateFormatter = 'moment';
            calendarConfig.allDateFormats.moment.date.hour = 'h:mm A';
            calendarConfig.allDateFormats.moment.title.day = 'dddd D MMMM';
            calendarConfig.i18nStrings.weekNumber = 'Semana {week}';
            calendarConfig.displayAllMonthEvents = true;
            calendarConfig.showTimesOnWeekView = true;

        }]).controller('CitaController', CitaController);

    CitaController.$inject = ['Principal', '$scope', 'CitaByAccount', 'Cita', 'ParseLinks', 'AlertService', 'paginationConstants', 'calendarConfig', '$state','EventsByAccount','$ngConfirm','moment'];

    function CitaController(Principal, $scope, CitaByAccount, Cita, ParseLinks, AlertService, paginationConstants, calendarConfig, $state,EventsByAccount,$ngConfirm,moment) {

        var vm = this;
        moment.locale('es');
        vm.citas = [];
        vm.eventos = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = false;
        vm.currentSearch = "";
        vm.account = null;
        vm.calendarView = 'month';
        vm.viewDate = new Date();
        vm.cellIsOpen = true;
        vm.eventsLoad = [];


        var actions = [{
            label: '<i class=\'glyphicon glyphicon-pencil\'></i>',
            onClick: function (args) {
                $state.go('cita.edit', {id: args.calendarEvent.cita.id});
            }
        }, {
            label: '<i class=\'glyphicon glyphicon-remove\'></i>',
            onClick: function (args) {
                $state.go('cita.delete', {id: args.calendarEvent.cita.id});
            }
        }];

        vm.saveCita=function(cita){
            if (cita.id !== null) {
                Cita.update(cita, onSaveSuccess, onSaveError);
            } else {
                Cita.save(cita, onSaveSuccess, onSaveError);
            }
        };

        function onSaveSuccess (result) {
            $ngConfirm('La cita ha sido <strong>modificada</strong> satisfactoriamente');
            reset();
        }

        function onSaveError () {
            $ngConfirm('La cita no pudo ser <strong>modificada</strong>');
            reset();
        }

        vm.confirm = function (event) {
            $ngConfirm({
                title: 'Modificar Cita',
                content: '¿Desea <strong>modificar</strong> esta cita?. <br> Elija una opción',
                icon: 'fa fa-question-circle',
                animation: 'scale',
                closeAnimation: 'scale',
                opacity: 0.5,
                buttons: {
                    'confirm': {
                        text: 'Si, modificar',
                        btnClass: 'btn-info',
                        action: function () {
                            Cita.update(event.cita, onSaveSuccess, onSaveError);
                        }
                    },
                    'cancel': {
                        text: 'No',
                        btnClass: 'btn-warning'
                    }
                }
            })
        };

        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isClient=vm.account.authorities.includes("ROLE_CLIENTE");
                loadCitas();
                loadEventos();

            });
        }

        function loadCitas() {
            CitaByAccount.query({
                id: vm.account.id,
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.citas.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadEventos() {
            EventsByAccount.query({
                id: vm.account.id,
                page: vm.page,
                size: vm.itemsPerPage
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                for (var i = 0; i < data.length; i++) {
                    vm.eventos.push(data[i]);
                }
                vm.loadEvents();
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }



        vm.loadEvents = function () {
            for (var i = 0; i < vm.eventos.length; i++) {
                vm.eventos[i].startsAt=new Date(vm.eventos[i].startsAt);
                vm.eventos[i].endsAt=new Date(vm.eventos[i].endsAt);
                if (vm.eventos[i].cita.trabajo.estado==='EN_PROGRESO' && new Date(vm.eventos[i].cita.fechaYHora)>new Date()){
                    if (!vm.isClient){
                        vm.eventos[i].actions = actions;
                    }
                }
            }
        };

        vm.eventClicked = function (event) {
            if (!vm.isClient) {
                $state.go('cita-detail', {id: event.cita.id});
            }
        };

        vm.eventTimesChanged = function(event,calendarNewEventStart,calendarNewEventEnd) {

            event.startsAt = calendarNewEventStart;
            event.endsAt = calendarNewEventEnd;
            vm.viewDate = event.startsAt;
            event.cita.fechaYHora=event.startsAt;
            event.cita.duracion=moment.duration(moment(event.endsAt).diff(moment(event.startsAt))).asHours();
            vm.confirm(event);
        };

        vm.rangeSelected = function(startDate, endDate) {
            if (!vm.isClient) {
                vm.duracion=moment.duration(moment(endDate).diff(moment(startDate))).asHours();
                if (startDate>=new Date()) {
                    if (vm.duracion >= 1) {
                        $state.go('cita.new.calendar', {
                            fecha: moment(startDate).toISOString(),
                            duracion:vm.duracion});
                    }else {
                        $ngConfirm('La cita debe durar al menos <strong>una(1) hora</strong>');
                    }
                }else{
                    $ngConfirm('No puedes crear una cita en una <strong>fecha previa</strong>');
                }
            }
        };

        function reset() {
            vm.page = 0;
            vm.citas = [];
            vm.eventos=[];
            loadCitas();
            loadEventos();
        }

        function loadPage(page) {
            vm.page = page;
            loadCitas();
        }

    }
})();
