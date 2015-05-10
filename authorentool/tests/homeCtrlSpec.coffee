expect = chai.expect

describe 'test that homeCtrl', () ->

    beforeEach module('storyTellarCtrl')

    scope = {}
    ctrl = {}
    serverService = {
        getStoryList : sinon.stub().callsArgWith(0, "stories")
        createStory : sinon.stub()
    }
    location = {
        path : sinon.stub()
    }

    beforeEach inject ($rootScope, $controller) ->
        scope = $rootScope.$new()
        ctrl = $controller 'homeCtrl', {
            $scope : scope
            $location : location
            storytellerServer : serverService
        }

    it 'should call Stories', () ->
        expect(serverService.getStoryList).to.have.been.called

    it 'should initialize stories', () ->
        expect(scope.storys).to.be.equal "stories"

    it 'should call createStory', () ->
        scope.createStory()
        expect(serverService.createStory).to.have.been.calledOnce

    it 'should update location path if select', () ->
        scope.select(100)
        expect(location.path).to.have.been.calledWith "/story/100"

