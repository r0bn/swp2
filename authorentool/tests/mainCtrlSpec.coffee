# http://www.smashingmagazine.com/2014/10/07/introduction-to-unit-testing-in-angularjs/
expect = chai.expect

describe 'test that mainCtrl', () ->

    beforeEach module('mainApp')

    scope = {}
    ctrl = {}

    beforeEach inject ($rootScope, $controller) ->
        scope = $rootScope.$new()
        ctrl = $controller 'mainCtrl', { $scope : scope }

    it 'should initialize selectedFile', () ->
        expect(scope.selectedFile).to.equal ""

    it 'should initialize selectedFile2', () ->
        expect(scope.selectedFile2).to.equal ""

    it 'should setup xml editor options', () ->
        expect(scope.editorOptions).exist
        
    it 'should initialize storySelected', () ->
        expect(scope.storySelected).to.equal false

    it 'should handle story selected', () ->
        scope.handleStorySelected()
        expect(scope.storySelected).to.equal true
