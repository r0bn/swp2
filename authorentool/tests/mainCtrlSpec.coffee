# http://www.smashingmagazine.com/2014/10/07/introduction-to-unit-testing-in-angularjs/
expect = chai.expect

describe 'test that editorCtrl', () ->

    beforeEach module('storyTellarCtrl')

    scope = {}
    ctrl = {}
    routeParams = {}
    serverService = {}

    beforeEach inject ($rootScope, $controller) ->
        scope = $rootScope.$new()
        ctrl = $controller 'editorCtrl', {
            $scope : scope
            $routeParams : routeParams
            storytellerServer : serverService
        }

    it 'should initialize selectedFile', () ->
        expect(scope.selectedFile).to.equal ""

    it 'should initialize selectedFile2', () ->
        expect(scope.selectedFile2).to.equal ""

    it 'should setup xml editor options', () ->
        expect(scope.editorOptions).exist
        
