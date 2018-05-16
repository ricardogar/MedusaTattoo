'use strict';

describe('Controller Tests', function() {

    describe('Tatuador Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTatuador, MockTrabajo, MockRayaton;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTatuador = jasmine.createSpy('MockTatuador');
            MockTrabajo = jasmine.createSpy('MockTrabajo');
            MockRayaton = jasmine.createSpy('MockRayaton');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Tatuador': MockTatuador,
                'Trabajo': MockTrabajo,
                'Rayaton': MockRayaton
            };
            createController = function() {
                $injector.get('$controller')("TatuadorDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'medusaTattooApp:tatuadorUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
