# Client Logic #

The following code is a angularJS (https://angularjs.org/) Application.

    mainApp = angular.module "mainApp", ['ui.codemirror']

    # the main controller
    mainApp.controller "mainCtrl", ["$scope", "$http", ($scope, $http) ->

        # Codemirror Options
        # Details: https://codemirror.net/doc/manual.html#config
        $scope.editorOptions =
            #lineWrapping : true
            lineNumbers: true
            #readOnly: 'nocursor'
            mode: 'xml'
            #indentUnit : 2
            theme : "eclipse"

        # handles the event if a user choose a story to edit
        $scope.storySelected = false
        $scope.handleStorySelected = (story) ->
            $scope.storySelected = true

            $http.get("http://api.dev.la/story/#{story.id}")
                .success (data) ->
                    $scope.xmlFile = data

        # Creates a story on the server
        $scope.createStory = () ->
            $http.post("http://api.dev.la/createstory", $scope.storys[0])
                .success () ->
                    console.log "created"

        # this will be initial executed and get all available story's
        $http.get("http://api.dev.la/stories")
            .success (data) ->
                $scope.storys = data

        # This is dummy data for demmo reasons
        $scope.mediaData = [
            {
                id : 1
                name : "Cover"
                type : "image"
            },
            {
                id : 2
                name : "ReferenceBar"
                type : "image"
            },
            {
                id : 3
                name : "Introduction"
                type : "movie"
            },
            {
                id : 4
                name : "FinalScene"
                type : "movie"
            }
        ]

    ]

