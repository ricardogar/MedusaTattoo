(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('RayatonReportController', RayatonReportController);

    RayatonReportController.$inject = ['$scope','DataUtils', 'Rayaton', 'RayatonMoney','RayatonWorks','ParseLinks', 'AlertService'];

    function RayatonReportController($scope,DataUtils, Rayaton,RayatonMoney,RayatonWorks,ParseLinks, AlertService) {
    
        var vm = this;
        $scope.start = moment();
        $scope.end = moment().add(1, 'days').add(1, 'hours');
        $scope.presets = [
          {
            'name': 'Esta semana',
            'start': moment().startOf('week').startOf('day'),
            'end': moment().endOf('week').endOf('day'),
          }, {
            'name': 'Este mes',
            'start': moment().startOf('month').startOf('day'),
            'end': moment().endOf('month').endOf('day'),
          }, {
            'name': 'Este año',
            'start': moment().startOf('year').startOf('day'),
            'end': moment().endOf('year').endOf('day'),
          }
        ];
          
        //vm.rayatons = Rayaton.query();

        vm.rayatons=[];
		vm.fechasMoney=[];
		vm.fechasWorks=[];
		vm.money=[];
		vm.works=[];
		loadMoney();
		loadWorks();
        vm.fecha_i = moment($scope.start).format();
        vm.fecha_f = moment($scope.end).format();
        $scope.changed = function () {
          console.log('Cambio la fecha');
            vm.fecha_i = moment($scope.start).format();
            vm.fecha_f = moment($scope.end).format();
            vm.rayatons=[];
            vm.fechasMoney=[];
            vm.fechasWorks=[];
            vm.money=[];
            vm.works=[];
            loadMoney();
            loadWorks();  
        };
		function loadMoney () {

            RayatonMoney.query({minDate:vm.fecha_i,
            maxDate:vm.fecha_f}, onSuccess, onError);

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
            RayatonWorks.query({minDate:vm.fecha_i,
            maxDate:vm.fecha_f}, onSuccess, onError);

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
                type: 'bar'
            },
            title: {
                text: 'Trabajos finalizados por Rayatón'
            },
            xAxis: {
                categories: vm.works
            },

            series: [{
                data: vm.fechasWorks
            }]
        };

    }
})();
