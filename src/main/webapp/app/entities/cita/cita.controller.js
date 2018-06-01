(function () {
    'use strict';

    angular
        .module('medusaTattooApp')
        .config(['calendarConfig', function (calendarConfig) {

            // View all available config
            console.log(calendarConfig);

            calendarConfig.dateFormatter = 'moment';
            calendarConfig.allDateFormats.moment.date.hour = 'h:mm A';
            calendarConfig.allDateFormats.moment.title.day = 'ddd D MMM';
            calendarConfig.i18nStrings.weekNumber = 'Semana {week}';
            calendarConfig.displayAllMonthEvents = true;
            calendarConfig.showTimesOnWeekView = true;

        }]).controller('CitaController', CitaController);

    CitaController.$inject = ['Principal', '$scope', 'CitaByAccount', 'Cita', 'ParseLinks', 'AlertService', 'paginationConstants', 'calendarConfig', '$state'];

    function CitaController(Principal, $scope, CitaByAccount, Cita, ParseLinks, AlertService, paginationConstants, calendarConfig, $state) {

        var vm = this;

        vm.citas = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        vm.currentSearch = "";
        vm.account = null;
        vm.calendarView = 'month';
        vm.viewDate = new Date();
        vm.cellIsOpen = true;
        vm.calendarTitle = "TITULO DEL CALENDARIO";
        vm.eventsLoad = [];

        var actions = [{
            label: '<i class=\'glyphicon glyphicon-pencil\'></i>',
            onClick: function (args) {
                $state.go('cita.edit', {id: args.calendarEvent.idCita});
            }
        }, {
            label: '<i class=\'glyphicon glyphicon-remove\'></i>',
            onClick: function (args) {
                $state.go('cita.delete', {id: args.calendarEvent.idCita});
            }
        }];
        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isClient=vm.account.authorities.includes("ROLE_CLIENTE");
                loadAll();

            });
        }

        function loadAll() {
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
                vm.loadEvents();
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.loadEvents = function () {
            vm.event = {};
            vm.eventsLoad = [];
            for (var i = 0; i < vm.citas.length; i++) {
                //reinicar variable
                vm.event = {};
                //establecer horarios de citas
                vm.fechaInicio = new Date(vm.citas[i].fechaYHora);
                vm.fechaFin = moment(vm.citas[i].fechaYHora).add(vm.citas[i].duracion, 'hours').toDate();
                //establecer atributos del evento que se carga en el calendario
                if (vm.isClient) {
                    vm.event.title='Tatuaje en: '+vm.citas[i].trabajo.sede.nombre+' ('+vm.citas[i].trabajo.sede.direccion+') a las ' ;
                }else {
                    vm.event.title = vm.citas[i].trabajo.nombre;
                }

                vm.event.color = calendarConfig.colorTypes.info;
                vm.event.startsAt = vm.fechaInicio;
                vm.event.endsAt = vm.fechaFin;
                if (!vm.isClient) {
                    vm.event.draggable = true;
                    vm.event.resizable = true;
                }
                vm.event.idCita = vm.citas[i].id;
                if (vm.account.authorities.includes())
                if (vm.citas[i].trabajo.estado==='EN_PROGRESO' && vm.fechaInicio>new Date() && !vm.isClient){
                    vm.event.actions = actions;
                }
                vm.eventsLoad.push(vm.event);
            }
        };

        vm.eventClicked = function (event) {
            if (!vm.isClient) {
                $state.go('cita-detail', {id: event.idCita});
            }
        };


        function reset() {
            vm.page = 0;
            vm.citas = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }

    }
})();
