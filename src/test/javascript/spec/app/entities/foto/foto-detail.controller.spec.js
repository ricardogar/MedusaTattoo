'use strict';

describe('Controller Tests', function() {

    describe('Foto Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockFoto, MockPalabraClave, MockTrabajo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockFoto = jasmine.createSpy('MockFoto');
            MockPalabraClave = jasmine.createSpy('MockPalabraClave');
            MockTrabajo = jasmine.createSpy('MockTrabajo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Foto': MockFoto,
                'PalabraClave': MockPalabraClave,
                'Trabajo': MockTrabajo
            };
            createController = function() {
                $injector.get('$controller')("FotoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'medusaTattooApp:fotoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
