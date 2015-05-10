# Client Logic #

The following code is a angularJS (https://angularjs.org/) Application.

    mainApp = angular.module "mainApp", ['ui.codemirror']

    # the main controller
    mainApp.controller "mainCtrl", ["$scope", "$http", "storytellerServer", ($scope, $http, $server) ->

        $scope.selectedFile = ""
        $scope.selectedFile2 = ""

        # Codemirror Options
        # Details: https://codemirror.net/doc/manual.html#config
        $scope.editorOptions =
            lineWrapping : true
            lineNumbers: true
            #readOnly: 'nocursor'
            mode: 'application/xml'
            #indentUnit : 2
            theme : "eclipse"
            foldGutter : true
            gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter"]

        # handles the event if a user choose a story to edit
        $scope.storySelected = false
        $scope.handleStorySelected = (story) ->
            $scope.storySelected = true

        $http.get("http://api.dev.la/story/1")
            .success (data) ->
                $scope.xmlFile = data

        # Creates a story on the server
        $scope.createStory = () ->
            $http.post("http://api.dev.la/createstory", $scope.storys[0])
                .success () ->
                    console.log "created"

        # this saves the current xml file
        $scope.saveXML = () ->
            console.log $scope.selectedFile
            test = $scope.storys[0]
            test.xml = $scope.xmlFile
            delete test.id
            $server.uploadMediaFile [$scope.selectedFile, $scope.selectedFile2 ], test

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


    mainApp.directive 'fileModel', [ () ->
        {
            restrict: 'A'
            scope :
                fileModel : '='
            link: (scope, element, attrs) ->
                
                element.bind 'change', () ->
                    scope.$apply () ->
                        scope.fileModel = element[0].files[0]
                        console.log scope.fileModel
        }
    ]

