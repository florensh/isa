'use strict';

describe('Controller Tests', function() {

    describe('Visit Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockVisit, MockStore, MockVisitor;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockVisit = jasmine.createSpy('MockVisit');
            MockStore = jasmine.createSpy('MockStore');
            MockVisitor = jasmine.createSpy('MockVisitor');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Visit': MockVisit,
                'Store': MockStore,
                'Visitor': MockVisitor
            };
            createController = function() {
                $injector.get('$controller')("VisitDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'isaApp:visitUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
