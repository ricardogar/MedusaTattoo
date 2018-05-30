(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .controller('SedesReportController', SedesReportController);

    SedesReportController.$inject = ['$scope','DataUtils', 'Sedes', 'SedesMoney','SedesWorks','ParseLinks', 'AlertService'];

    function SedesReportController($scope,DataUtils, Sedes,SedesMoney,SedesWorks,ParseLinks, AlertService) {
    
        var vm = this;
        setupMoment();
        init();

        $scope.changed = function () {
            init();
        }

        function loadMoney () {
            SedesMoney.query({minDate:vm.fecha_i,
            maxDate:vm.fecha_f}, onSuccess, onError);
            function onSuccess(data, headers) {
                for (var i = 0; i < data.length; i++) {
                    vm.fechasMoney.push(data[i][1]);
                    vm.money.push(data[i][2]);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        function loadWorks () {
            SedesWorks.query({minDate:vm.fecha_i,
            maxDate:vm.fecha_f}, onSuccess, onError);
            function onSuccess(data, headers) {
                for (var i = 0; i < data.length; i++) {
                    vm.fechasWorks.push(data[i][1]);
                    vm.works.push(data[i][2]);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        function init(){
            vm.fecha_i = moment($scope.start).format();
            vm.fecha_f = moment($scope.end).format();
            vm.fechasMoney=[];
            vm.fechasWorks=[];
            vm.money=[];
            vm.works=[];
            loadMoney();
            loadWorks();
            $scope.configChartMoney = {
                chart: {
                    type: 'column'
                },
                title: {
                    text: 'Dinero obtenido por sedes'
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
                    text: 'Trabajos finalizados por sede'
                },
                xAxis: {
                    categories: vm.fechasWorks
                },

                series: [{
                    data: vm.works
                }]
            };
        }

        function setupMoment(){
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
        }
    }
})();
