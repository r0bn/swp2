# Component #

    mainApp = angular.module "mainApp", ['ui.codemirror']

    mainApp.controller "mainCtrl", ["$scope", "$http", ($scope, $http) ->
        # Codemirror
        $scope.editorOptions =
            #lineWrapping : true
            lineNumbers: true
            #readOnly: 'nocursor'
            mode: 'xml'
            #indentUnit : 2
            theme : "eclipse"


        $scope.storySelected = false
        $scope.handleStorySelected = (story) ->
            $scope.storySelected = true

            $http.get("http://api.dev.la/story/#{story.id}")
                .success (data) ->
                    $scope.xmlFile = data

        $http.get("http://api.dev.la/stories")
            .success (data) ->
                $scope.storys = data

        $scope.createStory = () ->
            window.location.href='../createStory.html'

        $scope.deleteStory = () ->
            console.log("Gelöscht")

        $scope.getFormulars = () ->
            document.getElementById("createStoryRow").style.display="none"
            document.getElementById("Formular").style.display="block"

        $scope.getNewFeatureElement = (counter) ->
                copyForm = document.getElementById("NeuesFeature")
                stuff = copyForm.cloneNode(true)
                stuff.style.display="block"
                counter = 1 if counter == undefined
                stuff.id="NeuesFeature_" + counter
                document.getElementById("column").appendChild(stuff)
                stuff.scrollIntoView(true)

        $scope.tabbed_pain = (activeTabID,activeContentID, passiveTabID, passiveContentID) ->
                activeTab=document.getElementById(activeTabID)
                activeTab.classList.add("active")
                activeContent=document.getElementById(activeContentID)
                activeContent.style.display="block"
                passiveTab=document.getElementById(passiveTabID)
                passiveTab.classList.remove("active")
                passiveContent=document.getElementById(passiveContentID)
                passiveContent.style.display="none"

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

