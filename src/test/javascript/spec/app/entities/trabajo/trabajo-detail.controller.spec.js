'use strict';

describe('Controller Tests', function() {

    describe('Trabajo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTrabajo, MockPago, MockCita, MockFoto, MockTatuador, MockCliente, MockSede;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTrabajo = jasmine.createSpy('MockTrabajo');
            MockPago = jasmine.createSpy('MockPago');
            MockCita = jasmine.createSpy('MockCita');
            MockFoto = jasmine.createSpy('MockFoto');
            MockTatuador = jasmine.createSpy('MockTatuador');
            MockCliente = jasmine.createSpy('MockCliente');
            MockSede = jasmine.createSpy('MockSede');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Trabajo': MockTrabajo,
                'Pago': MockPago,
                'Cita': MockCita,
                'Foto': MockFoto,
                'Tatuador': MockTatuador,
                'Cliente': MockCliente,
                'Sede': MockSede
            };
            createController = function() {
                $injector.get('$controller')("TrabajoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'medusaTattooApp:trabajoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
