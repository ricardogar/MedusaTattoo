'use strict';

describe('Controller Tests', function() {

    describe('Inscripcion Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockInscripcion, MockRayaton, MockSede;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockInscripcion = jasmine.createSpy('MockInscripcion');
            MockRayaton = jasmine.createSpy('MockRayaton');
            MockSede = jasmine.createSpy('MockSede');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Inscripcion': MockInscripcion,
                'Rayaton': MockRayaton,
                'Sede': MockSede
            };
            createController = function() {
                $injector.get('$controller')("InscripcionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'medusaTattooApp:inscripcionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
