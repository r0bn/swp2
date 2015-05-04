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

        #jQuery Namespace Binding
        (($) ->
            ) jQuery

        # jQuery document.ready() function
        $ ->
            initDropdownClicks()
            googleMap() 
            graph()
            return

        initDropdownClicks = () ->
            #DropdownMenu Radius
            $("#ddnme").click -> 
                $("#ddnradius").val("Meter")
                $("#ddnradius").html("Meter <span class='caret' />")
            $("#ddnkm").click -> 
                $("#ddnradius").val("Kilometer")
                $("#ddnradius").html("Kilometer <span class='caret' />")

            #DropdownMenu Size
            $("#ddnkb").click -> 
                $("#ddnsize").val("KB")
                $("#ddnsize").html("KB <span class='caret' />")
            $("#ddnmb").click -> 
                $("#ddnsize").val("MB")
                $("#ddnsize").html("MB <span class='caret' />")
            $("#ddngb").click -> 
                $("#ddnsize").val("KB")
                $("#ddnsize").html("GB <span class='caret' />")

        $scope.createNewFeature = (counter) ->
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
                btnEinklappen("#btnFeatureEinklappen_" + counter, "#NeuesFeatureContent_" + counter)

                $("#btnCreateInteraction_" + counter).attr("interactionCounter", counter)
                # Click Event für btnCreateInteraction
                $("#btnCreateInteraction_" + counter).click ->
                    if ($("#ddnInteractions_" + counter).val() == "Item")
                        createItem(counter)
                    else if ($("#ddnInteractions_" + counter).val() == "Quiz")
                        createQuiz(counter)
                    else if ($("#ddnInteractions_" + counter).val() == "WayChooser")
                        createChooser(counter)
                # Click Events für ddnInteraction
                initDdnInteraction(counter)

                # Neues Feature Button machen.
                button = document.getElementById("btnFeature");
                button.parentNode.removeChild(button);
                document.getElementById("Features").appendChild(button)
                button.scrollIntoView(true)

        initDdnInteraction = (counter) ->
                $("#ddnWayChooser_" + counter).click ->
                    $("#ddnInteractions_" + counter).val("WayChooser")
                    $("#ddnInteractions_" + counter).html("WayChooser <span class='caret'/>")
                $("#ddnQuiz_" + counter).click ->
                    $("#ddnInteractions_" + counter).val("Quiz")
                    $("#ddnInteractions_" + counter).html("Quiz <span class='caret'/>")
                $("#ddnItem_" + counter).click ->
                    $("#ddnInteractions_" + counter).val("Item")
                    $("#ddnInteractions_" + counter).html("Item <span class='caret'/>")

        createItem = (counter) ->
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
            btnEinklappen("#btnItemEinklappen_" + interactionCounter, "#NeuesItemContent_" + interactionCounter)
                    
            # ItemName Trigger
            $("#itemID_"+interactionCounter).keyup ->
                text = "Item: " + $("#itemID_"+interactionCounter).val()
                $("#NeuesItemFieldset_"+interactionCounter).text(text)


        createQuiz = (counter) ->
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
    
            btnEinklappen("#btnQuizDelete_" + interactionCounter, "#NeuesQuizContent_" + interactionCounter)
            # quizID Trigger
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

        createChooser = (counter) ->
                copyForm = document.getElementById("Neu_" + $("#ddnInteractions_" + counter).val())
                interactionCounter = $("#btnCreateInteraction_" + counter).attr("interactionCounter")
                interactionCounter++
                $("#btnCreateInteraction_" + counter).attr("interactionCounter", interactionCounter)
                stuff = copyForm.cloneNode(true)
                stuff.id = stuff.id + "_" + interactionCounter
                stuff.style.display="block"
                document.getElementById("NeuesFeatureContent_"+counter).appendChild(stuff)
                $("#" + stuff.id).find("#NeuerWaychooserFieldset").attr("id", "NeuerWaychooserFieldset_" +interactionCounter)
                $("#" + stuff.id).find("#NeuerWaychooserContent").attr("id", "NeuerWaychooserContent_" +interactionCounter)
                $("#" + stuff.id).find("#btnWaychooserControlGroup").attr("id", "btnWaychooserControlGroup_" +interactionCounter)
                $("#" + stuff.id).find("#btnWaychooserEinklappen").attr("id", "btnWaychooserEinklappen_" +interactionCounter)
                $("#" + stuff.id).find("#btnWaychooserDelete").attr("id", "btnWaychooserDelete_" +interactionCounter)
                $("#" + stuff.id).find("#waychooserID").attr("id", "waychooserID_" +interactionCounter)
                $("#" + stuff.id).find("#waychooserFeatureRef").attr("id", "waychooserFeatureRef_" +interactionCounter)
                $("#" + stuff.id).find("#waychooserQuestion").attr("id", "waychooserQuestion_" +interactionCounter)
                $("#" + stuff.id).find("#btnWaychooserAnswer").attr("id", "btnWaychooserAnswer_" +interactionCounter)


                # Click Event für btnWaychooserDelete
                $("#btnWaychooserDelete_" + interactionCounter).click ->
                   $("#Neu_Waychooser_" + interactionCounter).remove() if (confirm('Möchten Sie den Waychooser wirklich löschen?'))
        
                # Click Event für btnWaychooserEinklappen
                btnEinklappen("#btnWaychooserEinklappen_" + interactionCounter, "#NeuerWaychooserContent_" + interactionCounter)

                # WaychooserName Trigger
                $("#waychooserID_"+interactionCounter).keyup ->
                    text = "Waychooser: " + $("#waychooserID_"+interactionCounter).val()
                    $("#NeuerWaychooserFieldset_"+interactionCounter).text(text)
                
                # Click Event für btnWaychooserAnswer
                $("#btnWaychooserAnswer_" + interactionCounter).click ->
                    waychooserAnswerCounter = $("#btnWaychooserAnswer_" + interactionCounter).attr("waychooserAnswerCounter")
                    waychooserAnswerCounter++
                    $("#btnWaychooserAnswer_" + interactionCounter).attr("waychooserAnswerCounter", waychooserAnswerCounter)
                    copyAnswer = document.getElementById("WaychooserAnswer")
                    answer = copyAnswer.cloneNode(true)
                    answer.id = answer.id + "_" + waychooserAnswerCounter
                    answer.style.display="block"
                    document.getElementById("NeuerWaychooserContent_" + interactionCounter).appendChild(answer)
                    $("#" + answer.id).find("#lblWaychooserAnswerID").attr("id", "lblWaychooserAnswerID_" +waychooserAnswerCounter)
                    $("#" + answer.id).find("#waychooserAnswerID").attr("id", "waychooserAnswerID_" +waychooserAnswerCounter)
                    $("#" + answer.id).find("#lblWaychooserAnswerText").attr("id", "lblWaychooserAnswerText_" +waychooserAnswerCounter)
                    $("#" + answer.id).find("#waychooserAnswerText").attr("id", "waychooserAnswerText_" +waychooserAnswerCounter)
                    $("#" + answer.id).find("#lblWaychooserAnswerItemRef").attr("id", "lblWaychooserAnswerItemRef_" +waychooserAnswerCounter)
                    $("#" + answer.id).find("#waychooserAnswerItemRef").attr("id", "waychooserAnswerItemRef_" +waychooserAnswerCounter)
                    $("#" + answer.id).find("#lblWaychooserAnswerFeatureRef").attr("id", "lblWaychooserAnswerFeatureRef_" +waychooserAnswerCounter)
                    $("#" + answer.id).find("#waychooserAnswerFeatureRef").attr("id", "waychooserAnswerFeatureRef_" +waychooserAnswerCounter)
                    $("#" + answer.id).find("#btnWaychooserAnswerDelete").attr("id", "btnWaychooserAnswerDelete_" + waychooserAnswerCounter)

                    # Click Event für btnWaychooserDelete
                    $("#btnWaychooserAnswerDelete_" + waychooserAnswerCounter).click ->
                        $("#" + answer.id).remove() if (confirm('Möchten Sie die Antwort wirklich löschen?'))

        btnEinklappen = (button, content) ->
            # Click Event für btnQuizEinklappen
            $(button).click ->
                if $(content).is( ":hidden" )
                    $(content).show( "slow" )
                    $(button).find("#resize").addClass('glyphicon-resize-small')
                    $(button).find("#resize").removeClass('glyphicon-resize-full')
                else
                    $(content).slideUp("slow")
                    $(button).find("#resize").removeClass('glyphicon-resize-small')
                    $(button).find("#resize").addClass('glyphicon-resize-full')

        $scope.tabSwitch = (activeTabID) ->
                if (activeTabID == "MedienBibTab") 
                    # Active Tabs
                    $("#MedienBibTab").addClass("active")

                    # Passive Tabs
                    $("#GraEditorTab").removeClass("active")
                    $("#XMLTab").removeClass("active")

                    # Active Content
                    $("#MedienEditor").css("display", "block")
                    
                    # Passive Content
                    $("#GraEditor").css("display","none")
                    $("#XML").css("display","none")
                    
                    return
                else if (activeTabID == "GraEditorTab") 
                    # Active Tabs
                    $("#GraEditorTab").addClass("active")

                    # Passive Tabs
                    $("#MedienBibTab").removeClass("active")
                    $("#XMLTab").removeClass("active")
                    
                    # Active Content
                    $("#GraEditor").css("display", "block")

                    # Passive Content
                    $("#XML").css("display","none")
                    $("#MedienEditor").css("display","none")
                    
                    return
                else if (activeTabID == "XMLTab") 
                    # Active Tabs
                    $("#XMLTab").addClass("active")

                    # Passive Tabs
                    $("#GraEditorTab").removeClass("active")
                    $("#MedienBibTab").removeClass("active")

                    # Active Content
                    $("#XML").css("display", "block")

                    # Passive Content
                    $("#MedienEditor").css("display","none")
                    $("#GraEditor").css("display","none")
                    return

        graph = () ->
            nodes = [
                  {
                    id: 1
                    label: 'Node 1'
                  }
                  {
                    id: 2
                    label: 'Node 2'
                  }
                  {
                    id: 3
                    label: 'Node 3'
                  }
                  {
                    id: 4
                    label: 'Node 4'
                  }
                  {
                    id: 5
                    label: 'Node 5'
                  }
            ]
                # create an array with edges
            edges = [
                  {
                    from: 1
                    to: 2
                  }
                  {
                    from: 1
                    to: 3
                  }
                  {
                    from: 2
                    to: 4
                  }
                  {
                    from: 2
                    to: 5
                  }
            ]

            container = document.getElementById('divDependencyBox');
            data = {
                nodes: nodes,
                edges: edges
            }
            network = new vis.Network(container, data, {});
            return

        googleMap = () ->
                mapOptions = 
                  zoom: 8
                  center: new (google.maps.LatLng)(48.7760745003604, 9.172875881195068)
                map = new (google.maps.Map)($('#gmeg_map_canvas')[0], mapOptions)
                input = document.getElementById('inputMapSearch')
                map.controls[google.maps.ControlPosition.TOP_LEFT].push input
                searchBox = new (google.maps.places.SearchBox)(input)
                markers = []

                google.maps.event.addListener searchBox, 'places_changed', ->
                  `var marker`
                  `var i`
                  places = searchBox.getPlaces()
                  if places.length == 0
                    return
                  i = 0
                  marker = undefined
                  if typeof markers != 'undefined'
                    while marker = markers[i]
                        marker.setMap null
                        i++
                  # For each place, get the icon, place name, and location.
                  markers = []
                  bounds = new (google.maps.LatLngBounds)
                  i = 0
                  place = undefined
                  while place = places[i]
                    image = 
                      url: place.icon
                      size: new (google.maps.Size)(71, 71)
                      origin: new (google.maps.Point)(0, 0)
                      anchor: new (google.maps.Point)(17, 34)
                      scaledSize: new (google.maps.Size)(25, 25)
                    # Create a marker for each place.
                    setAllMap(null, markers)
                    markers = []
                    marker = new (google.maps.Marker)(
                      map: map
                      icon: image
                      title: place.name
                      position: place.geometry.location)
                    markers.push marker
                    bounds.extend place.geometry.location
                    i++
                  map.fitBounds bounds
                  return

                $(window).resize ->
                  google.maps.event.trigger map, 'resize'
                  return
                google.maps.event.addListener map, 'click', (event) ->
                    lat = event.latLng.lat()
                    lng = event.latLng.lng()
                    $("#inputMapSearch").val(lat + ", " + lng)
                    $("#LngLocation").val(lng)
                    $("#LatLocation").val(lat)
                    i = 0
                    while i < markers.length
                        markers[i].setMap null
                        i++
                    addMarker(event.latLng, markers, map);
                    return
                lightBox(map)
            
        lightBox = (map) ->
                $lightbox = $('#lightbox')
                $('[data-target="#lightbox"]').on 'click', (event) ->
                  $mapDiv = $(this).find('gmeg_map_canvas')
                  css = 
                    'maxWidth': $(window).width() - 100
                    'maxHeight': $(window).height() - 100
                  $lightbox.find('.close').addClass 'hidden'
                  $mapDiv.css css
                  google.maps.event.trigger map, 'resize'
                  return
                $lightbox.on 'shown.bs.modal', (e) ->
                  $mapDiv = $(this).find('gmeg_map_canvas')
                  $lightbox.find('.modal-dialog').css 'width': $mapDiv.width()
                  $lightbox.find('.close').removeClass 'hidden'
                  google.maps.event.trigger map, 'resize'
                  return

        addMarker = (location, markers, map) ->
            marker = new google.maps.Marker({
                position: location,
                map: map
            });
            markers.push(marker)
            return


        setAllMap = (map, markers) ->
          i = 0
          while i < markers.length
            markers[i].setMap map
            i++
          return
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

