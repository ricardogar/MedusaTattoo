(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('RayatonReportController', RayatonReportController);

    RayatonReportController.$inject = ['$scope','DataUtils', 'Rayaton', 'RayatonMoney','RayatonWorks','ParseLinks', 'AlertService'];

    function RayatonReportController($scope,DataUtils, Rayaton,RayatonMoney,RayatonWorks,ParseLinks, AlertService) {

        var vm = this;
        //vm.rayatons = Rayaton.query();

        vm.rayatons=[];

		vm.fechasMoney=[];
		vm.fechasWorks=[];
		vm.money=[];
		vm.works=[];
		loadMoney();
		loadWorks();
		function loadMoney () {
            RayatonMoney.query({minDate:"2017-05-27T05:00:00.000Z",
            maxDate:"2019-05-27T05:00:00.000Z"}, onSuccess, onError);

            function onSuccess(data, headers) {
                for (var i = 0; i < data.length; i++) {
					vm.obje = data[i];
                    vm.rayatons.push(vm.obje);
					vm.fechasMoney.push(vm.obje[1]);
					vm.money.push(vm.obje[2]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

		function loadWorks () {
            RayatonWorks.query({minDate:"2017-05-27T05:00:00.000Z",
            maxDate:"2019-05-27T05:00:00.000Z"}, onSuccess, onError);

            function onSuccess(data, headers) {
                for (var i = 0; i < data.length; i++) {
					vm.obje = data[i];
					vm.fechasWorks.push(vm.obje[1]);
					vm.works.push(vm.obje[2]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }



        $scope.configChartMoney = {
            chart: {
                type: 'column'
            },
            title: {
                text: 'Dinero por Rayatón'
            },
            xAxis: {
                categories: vm.fechasMoney
            },

            series: [{
                data: vm.money
            }]
        };
		 $scope.configChartWorks = {
            chart: {
                type: 'column'
            },
            title: {
                text: 'Trabajos finalizados por Rayatón'
            },
            xAxis: {
                categories: vm.fechasWorks
            },

            series: [{
                data: vm.works
            }]
        };

    }
})();
