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
            mapOptions = 
              zoom: 8
              center: new (google.maps.LatLng)(48.7760745003604, 9.172875881195068)
            map = new (google.maps.Map)($('#gmeg_map_canvas')[0], mapOptions)
            $(window).resize ->
              google.maps.event.trigger map, 'resize'
              return
            google.maps.event.addListener map, 'click', (event) ->
                lat = event.latLng.lat()
                lng = event.latLng.lng()
                $("#inputMapSearch").val(lat + ", " + lng)
                $("#location").val(lat + ", " + lng)
            $lightbox = $('#lightbox')
            $('[data-target="#lightbox"]').on 'click', (event) ->
              $img = $(this).find('gmeg_map_canvas')
              src = $img.attr('src')
              alt = $img.attr('alt')
              css = 
                'maxWidth': $(window).width() - 100
                'maxHeight': $(window).height() - 100
              $lightbox.find('.close').addClass 'hidden'
              $lightbox.find('gmeg_map_canvas').attr 'src', src
              $lightbox.find('gmeg_map_canvas').attr 'alt', alt
              $lightbox.find('gmeg_map_canvas').css css
              google.maps.event.trigger map, 'resize'
              return
            $lightbox.on 'shown.bs.modal', (e) ->
              $img = $lightbox.find('gmeg_map_canvas')
              $lightbox.find('.modal-dialog').css 'width': $img.width()
              $lightbox.find('.close').removeClass 'hidden'
              google.maps.event.trigger map, 'resize'
              return
               
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
                $("#" + stuff.id).find("#ddnWayChooser").attr("id","ddnWayChooser_" + counter)
                $("#" + stuff.id).find("#ddnQuiz").attr("id","ddnQuiz_" + counter)
                $("#" + stuff.id).find("#ddnItem").attr("id","ddnItem_" + counter)
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
                $("#btnCreateInteraction_" + counter).attr("interactionCounter", counter)
                # Click Event für btnCreateInteraction
                $("#btnCreateInteraction_" + counter).click ->
                    if ($("#ddnInteractions_" + counter).val() == "Item")
                        copyForm = document.getElementById("Neu_" + $("#ddnInteractions_" + counter).val())
                        interactionCounter = $("#btnCreateInteraction_" + counter).attr("interactionCounter")
                        interactionCounter++
                        $("#btnCreateInteraction_" + counter).attr("interactionCounter", interactionCounter)
                        stuff = copyForm.cloneNode(true)
                        stuff.id = stuff.id + "_" + interactionCounter
                        stuff.style.display="block"
                        document.getElementById("NeuesFeatureContent_"+counter).appendChild(stuff)
                        
                        $("#" + stuff.id).find("#NeuesItemFieldset").attr("id", "NeuesItemFieldset_" +interactionCounter)
                        $("#" + stuff.id).find("#NeuesItemContent").attr("id", "NeuesItemContent_" +interactionCounter)
                        $("#" + stuff.id).find("#btnItemControlGroup").attr("id", "btnItemControlGroup_" +interactionCounter)
                        $("#" + stuff.id).find("#btnItemEinklappen").attr("id", "btnItemEinklappen_" +interactionCounter)
                        $("#" + stuff.id).find("#btnItemDelete").attr("id", "btnItemDelete_" +interactionCounter)
                        $("#" + stuff.id).find("#itemID").attr("id", "itemID_" +interactionCounter)
                        $("#" + stuff.id).find("#itemDescription").attr("id", "itemDescription_" +interactionCounter)
                        $("#" + stuff.id).find("#itemFeatureRef").attr("id", "itemFeatureRef_" +interactionCounter)
                        $("#" + stuff.id).find("#itemIsCollected").attr("id", "itemIsCollected_" +interactionCounter)
                        $("#" + stuff.id).find("#lblItemID").attr("id", "lblItemID_" +interactionCounter)
                        $("#" + stuff.id).find("#lblItemIsCollected").attr("id", "lblItemIsCollected_" +interactionCounter)
                        $("#" + stuff.id).find("#lblItemDescription").attr("id", "lblItemDescription_" +interactionCounter)
                        $("#" + stuff.id).find("#lblItemFeatureRef").attr("id", "lblItemFeatureRef_" +interactionCounter)
                        $("#lblItemID_" + interactionCounter).attr("for", "itemID_"+interactionCounter)
                        $("#lblItemIsCollected_" + interactionCounter).attr("for", "itemIsCollected_"+interactionCounter)
                        $("#lblItemDescription_" + interactionCounter).attr("for", "itemDescription_"+interactionCounter)
                        $("#lblItemFeatureRef_" + interactionCounter).attr("for", "itemFeatureRef_"+interactionCounter)
                        $("#itemFeatureRef_" + interactionCounter).val("NeuesFeature_" + counter)
                        # Click Event für btnItemDelete
                        $("#btnItemDelete_" + interactionCounter).click ->
                           $("#Neu_Item_" + interactionCounter).remove() if (confirm('Möchten Sie das Item wirklich löschen?'))
                
                        # Click Event für btnItemEinklappen
                        $("#btnItemEinklappen_" + interactionCounter).click ->
                            if $("#NeuesItemContent_" + interactionCounter).is( ":hidden" )
                                $("#NeuesItemContent_" + interactionCounter).show( "slow" )
                                $("#btnItemControlGroup_"  +interactionCounter).addClass("dropup")
                            else
                                $("#NeuesItemContent_" + interactionCounter).slideUp("slow")
                                $("#btnItemControlGroup_"  +interactionCounter).removeClass("dropup")
                        # ItemName Trigger
                        $("#itemID_"+interactionCounter).keyup ->
                            text = "Item: " + $("#itemID_"+interactionCounter).val()
                            $("#NeuesItemFieldset_"+interactionCounter).text(text)
                    else if ($("#ddnInteractions_" + counter).val() == "Quiz")
                        copyForm = document.getElementById("Neu_" + $("#ddnInteractions_" + counter).val())
                        interactionCounter = $("#btnCreateInteraction_" + counter).attr("interactionCounter")
                        interactionCounter++
                        $("#btnCreateInteraction_" + counter).attr("interactionCounter", interactionCounter)
                        stuff = copyForm.cloneNode(true)
                        stuff.id = stuff.id + "_" + interactionCounter
                        stuff.style.display="block"
                        document.getElementById("NeuesFeatureContent_"+counter).appendChild(stuff)
                        
                        $("#" + stuff.id).find("#NeuesQuizFieldset").attr("id", "NeuesQuizFieldset_" +interactionCounter)
                        $("#" + stuff.id).find("#NeuesQuizContent").attr("id", "NeuesQuizContent_" +interactionCounter)
                        $("#" + stuff.id).find("#btnQuizControlGroup").attr("id", "btnQuizControlGroup_" +interactionCounter)
                        $("#" + stuff.id).find("#btnQuizEinklappen").attr("id", "btnQuizEinklappen_" +interactionCounter)
                        $("#" + stuff.id).find("#btnQuizDelete").attr("id", "btnQuizDelete_" +interactionCounter)
                        $("#" + stuff.id).find("#quizID").attr("id", "quizID_" +interactionCounter)
                        $("#" + stuff.id).find("#quizFeatureRef").attr("id", "quizFeatureRef_" +interactionCounter)
                        $("#" + stuff.id).find("#quizOnTrue").attr("id", "quizOnTrue_" +interactionCounter)
                        $("#" + stuff.id).find("#quizOnFalse").attr("id", "quizOnFalse_" +interactionCounter)
                        $("#" + stuff.id).find("#quizQuestion").attr("id", "quizQuestion_" +interactionCounter)
                        $("#" + stuff.id).find("#btnQuizAnswer").attr("id", "btnQuizAnswer_" +interactionCounter)
                        # Click Event für btnQuizDelete
                        $("#btnQuizDelete_" + interactionCounter).click ->
                           $("#Neu_Quiz_" + interactionCounter).remove() if (confirm('Möchten Sie das Quiz wirklich löschen?'))
                
                        # Click Event für btnItemEinklappen
                        $("#btnQuizEinklappen_" + interactionCounter).click ->
                            if $("#NeuesQuizContent_" + interactionCounter).is( ":hidden" )
                                $("#NeuesQuizContent_" + interactionCounter).show( "slow" )
                                $("#btnQuizControlGroup_"  +interactionCounter).addClass("dropup")
                            else
                                $("#NeuesQuizContent_" + interactionCounter).slideUp("slow")
                                $("#btnQuizControlGroup_"  +interactionCounter).removeClass("dropup")
                        # ItemName Trigger
                        $("#quizID_"+interactionCounter).keyup ->
                            text = "Quiz: " + $("#quizID_"+interactionCounter).val()
                            $("#NeuesQuizFieldset_"+interactionCounter).text(text)
                        
                        # Click Event für btnQuizAnswer

                        
                        $("#btnQuizAnswer_" + interactionCounter).click ->
                            quizAnswerCounter = $("#btnQuizAnswer_" + interactionCounter).attr("quizAnswerCounter")
                            quizAnswerCounter++
                            $("#btnQuizAnswer_" + interactionCounter).attr("quizAnswerCounter", quizAnswerCounter)
                            copyAnswer = document.getElementById("Answer")
                            answer = copyAnswer.cloneNode(true)
                            answer.id = answer.id + "_" + quizAnswerCounter
                            answer.style.display="block"
                            document.getElementById("NeuesQuizContent_" + interactionCounter).appendChild(answer)
                            $("#" + answer.id).find("#lblQuizAnswerID").attr("id", "lblQuizAnswerID_" +quizAnswerCounter)
                            $("#" + answer.id).find("#quizAnswerID").attr("id", "quizAnswerID_" +quizAnswerCounter)
                            $("#" + answer.id).find("#lblQuizAnswerText").attr("id", "lblQuizAnswerText_" +quizAnswerCounter)
                            $("#" + answer.id).find("#quizAnswerText").attr("id", "quizAnswerText_" +quizAnswerCounter)
                            $("#" + answer.id).find("#btnQuizAnswerDelete").attr("id", "btnQuizAnswerDelete_" + quizAnswerCounter)
                            # Click Event für btnQuizDelete
                            $("#btnQuizAnswerDelete_" + quizAnswerCounter).click ->
                                $("#" + answer.id).remove() if (confirm('Möchten Sie die Antwort wirklich löschen?'))
                # Click Events für ddnInteraction
                $("#ddnWayChooser_" + counter).click ->
                    $("#ddnInteractions_" + counter).val("WayChooser")
                    $("#ddnInteractions_" + counter).html("WayChooser <span class='caret'/>")
                $("#ddnQuiz_" + counter).click ->
                    $("#ddnInteractions_" + counter).val("Quiz")
                    $("#ddnInteractions_" + counter).html("Quiz <span class='caret'/>")
                $("#ddnItem_" + counter).click ->
                    $("#ddnInteractions_" + counter).val("Item")
                    $("#ddnInteractions_" + counter).html("Item <span class='caret'/>")
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

