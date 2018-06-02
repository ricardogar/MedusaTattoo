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

    CitaController.$inject = ['Principal', '$scope', 'CitaByAccount', 'Cita', 'ParseLinks', 'AlertService', 'paginationConstants', 'calendarConfig', '$state','EventsByAccount'];

    function CitaController(Principal, $scope, CitaByAccount, Cita, ParseLinks, AlertService, paginationConstants, calendarConfig, $state,EventsByAccount) {

        var vm = this;

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
        vm.calendarTitle = "TITULO DEL CALENDARIO";
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
                console.log(JSON.stringify(actions[0]));
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
                console.log(vm.eventos);
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


        function reset() {
            vm.page = 0;
            vm.citas = [];
            loadCitas();
        }

        function loadPage(page) {
            vm.page = page;
            loadCitas();
        }

    }
})();
