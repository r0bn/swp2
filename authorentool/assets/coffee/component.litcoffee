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

        #jQuery Namespace Binding
        (($) ->
            ) jQuery
        #Test ob jQuery erfolgreich gebunden wurde (Siehe log im Browser)
        $ ->
            console.log("DOM is ready")

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
                counter = 1 if counter == undefined
                stuff.id="NeuesFeature_" + counter
                stuff.style.display="block"
                document.getElementById("Features").appendChild(stuff)
                # Setze die IDs neu
                $("#" + stuff.id).find("#NeuesFeatureFieldset").attr("id","NeuesFeatureFieldset_" + counter)
                $("#" + stuff.id).find("#btnNeuesFeatureDelete").attr("id","btnNeuesFeatureDelete_" + counter)
                $("#" + stuff.id).find("#btnFeatureEinklappen").attr("id","btnFeatureEinklappen_" + counter)
                $("#" + stuff.id).find("#NeuesFeatureContent").attr("id","NeuesFeatureContent_" + counter)
                $("#" + stuff.id).find("#lblInputFeature").attr("id","lblInputFeature_" + counter)
                $("#" + stuff.id).find("#inputFeature").attr("id","inputFeature_" + counter)
                $("#" + stuff.id).find("#btnControlGroup").attr("id","btnControlGroup_" + counter)
                $("#" + stuff.id).find("#btnCreateInteraction").attr("id","btnCreateInteraction_" + counter)
                $("#" + stuff.id).find("#ddnInteractions").attr("id","ddnInteractions_" + counter)

                # Labels neu setzen
                $("#lblInputFeature_" + counter).attr("for","inputFeature_" + counter)
                # FeatureName Trigger
                $("#inputFeature_"+counter).keyup ->
                    text = "Feature: " + $("#inputFeature_"+counter).val()
                    $("#NeuesFeatureFieldset_"+counter).text(text)

                # Click Event für btnNeuesFeatureDelete
                $("#btnNeuesFeatureDelete_" + counter).click ->
                   $("#NeuesFeature_" + counter).remove() if (confirm('Möchten Sie das Feature wirklich löschen?'))
        
                # Click Event für btnFeatureEinklappen
                $("#btnFeatureEinklappen_" + counter).click ->
                    if $("#NeuesFeatureContent_" + counter).is( ":hidden" )
                        $("#NeuesFeatureContent_" + counter).show( "slow" )
                        $("#btnControlGroup_"  +counter).addClass("dropup")
                    else
                        $("#NeuesFeatureContent_" + counter).slideUp("slow")
                        $("#btnControlGroup_"  +counter).removeClass("dropup")

                # Click Event für btnCreateInteraction
                $("#btnCreateInteraction_" + counter).click ->
                    $("#" + $("#ddnInteractions_" + counter).val()).show("slow")
                button = document.getElementById("btnFeature");
                button.parentNode.removeChild(button);
                document.getElementById("Features").appendChild(button)
                button.scrollIntoView(true)
        
        $scope.getNewPoiElement = (counter) ->
                copyForm = document.getElementById("NeuerPOI")
                stuff = copyForm.cloneNode(true)
                stuff.style.display="block"
                counter = 1 if counter == undefined
                stuff.id="POI_" + counter
                document.getElementById("POIS").appendChild(stuff)
                childs= document.getElementById(stuff.id).childNodes
                childs = childs[0].childNodes
                childs = childs[0].childNodes
                childs = childs[3].childNodes
                checkBox1 = childs[0].childNodes
                checkBox1[0].id = "inputAccessable_" +counter
                checkBox1[1].id = "lblAccessable_" +counter
                checkBox1[1].setAttribute("for", "inputAccessable_" +counter)
                checkBox2 = childs[1].childNodes
                checkBox2[0].id = "inputInternet_" +counter
                checkBox2[1].id = "lblInternet_" +counter
                checkBox2[1].setAttribute("for", "inputInternet_" +counter)
                button = document.getElementById("btnPOIS");
                button.parentNode.removeChild(button);
                document.getElementById("POIS").appendChild(button)
                button.scrollIntoView(true)

        $scope.createInteraction = () ->
                alert("Your book is overdue.");

        $scope.tabbed_pain = (activeTabID,activeContentID, passiveTabID, passiveContentID) ->
                jQuery(activeTabID).addClass("active")
                jQuery(activeContentID).css(display, "block")
                jQuery(passiveTabID).removeClass("active")
                jQuery(passiveContentID).css(display,"none")

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

