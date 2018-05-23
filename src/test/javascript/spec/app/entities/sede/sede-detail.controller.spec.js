'use strict';

describe('Controller Tests', function() {

    describe('Sede Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSede, MockTrabajo, MockInscripcion;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSede = jasmine.createSpy('MockSede');
            MockTrabajo = jasmine.createSpy('MockTrabajo');
            MockInscripcion = jasmine.createSpy('MockInscripcion');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Sede': MockSede,
                'Trabajo': MockTrabajo,
                'Inscripcion': MockInscripcion
            };
            createController = function() {
                $injector.get('$controller')("SedeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'medusaTattooApp:sedeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
