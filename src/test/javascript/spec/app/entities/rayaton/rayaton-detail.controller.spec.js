'use strict';

describe('Controller Tests', function() {

    describe('Rayaton Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockRayaton, MockInscripcion, MockTatuador;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockRayaton = jasmine.createSpy('MockRayaton');
            MockInscripcion = jasmine.createSpy('MockInscripcion');
            MockTatuador = jasmine.createSpy('MockTatuador');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Rayaton': MockRayaton,
                'Inscripcion': MockInscripcion,
                'Tatuador': MockTatuador
            };
            createController = function() {
                $injector.get('$controller')("RayatonDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'medusaTattooApp:rayatonUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
