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
                copyForm = document.getElementById("fhlNeuerStorypoint")
                stuff = copyForm.cloneNode(true)
                counter = 1 if counter == undefined
                stuff.id="fhlNeuerStorypoint_" + counter
                stuff.style.display="block"
                document.getElementById("fhlStorypoints").appendChild(stuff)
                # Setze die IDs neu
                $("#" + stuff.id).find("#lgdNeuerStorypointFieldset").attr("id","lgdNeuerStorypointFieldset_" + counter)
                $("#" + stuff.id).find("#btnNeuesStorypointDelete").attr("id","btnNeuesStorypointDelete_" + counter)
                $("#" + stuff.id).find("#btnStorypointEinklappen").attr("id","btnStorypointEinklappen_" + counter)
                $("#" + stuff.id).find("#fstNeuesStorypointContent").attr("id","fstNeuesStorypointContent_" + counter)
                $("#" + stuff.id).find("#lblInputStorypoint").attr("id","lblInputStorypoint_" + counter)
                $("#" + stuff.id).find("#inStorypoint").attr("id","inStorypoint_" + counter)
                $("#" + stuff.id).find("#bgpControlGroup").attr("id","bgpControlGroup_" + counter)
                $("#" + stuff.id).find("#btnCreateInteraction").attr("id","btnCreateInteraction_" + counter)
                $("#" + stuff.id).find("#ddnInteractions").attr("id","ddnInteractions_" + counter)
                $("#" + stuff.id).find("#ddnChooser").attr("id","ddnChooser_" + counter)
                $("#" + stuff.id).find("#ddnQuiz").attr("id","ddnQuiz_" + counter)
                $("#" + stuff.id).find("#ddnItem").attr("id","ddnItem_" + counter)
                # Labels neu setzen
                $("#lblInputStorypoint_" + counter).attr("for","inStorypoint_" + counter)
                # FeatureName Trigger
                $("#inStorypoint_"+counter).keyup ->
                    text = "Storypoint: " + $("#inStorypoint_"+counter).val()
                    $("#lgdNeuerStorypointFieldset_"+counter).text(text)

                $("#" + stuff.id).find("#btnSwitchDown").attr("id", "btnSwitchDown_" + counter)
                $("#" + stuff.id).find("#btnSwitchUp").attr("id", "btnSwitchUp_" + counter)
                btnSwitchDown("#btnSwitchDown_" + counter, "#" + stuff.id)
                btnSwitchUp("#btnSwitchUp_" + counter, "#" + stuff.id)

                # Click Event für btnNeuesStorypointDelete
                $("#btnNeuesStorypointDelete_" + counter).click ->
                   $("#fhlNeuerStorypoint_" + counter).remove() if (confirm('Möchten Sie den Storypoint wirklich löschen?'))
        
                # Click Event für btnStorypointEinklappen
                btnEinklappen("#btnStorypointEinklappen_" + counter, "#fstNeuesStorypointContent_" + counter)

                $("#btnCreateInteraction_" + counter).attr("interactionCounter", counter)
                # Click Event für btnCreateInteraction
                $("#btnCreateInteraction_" + counter).click ->
                    if ($("#ddnInteractions_" + counter).val() == "Item")
                        createItem(counter)
                    else if ($("#ddnInteractions_" + counter).val() == "Quiz")
                        createQuiz(counter)
                    else if ($("#ddnInteractions_" + counter).val() == "Chooser")
                        createChooser(counter)
                # Click Events für ddnInteraction
                initDdnInteraction(counter)

                # Neues Feature Button machen.
                button = document.getElementById("fgpStorypoint");
                button.parentNode.removeChild(button);
                document.getElementById("fhlStorypoints").appendChild(button)
                button.scrollIntoView(true)

        initDdnInteraction = (counter) ->
                $("#ddnChooser_" + counter).click ->
                    $("#ddnInteractions_" + counter).val("Chooser")
                    $("#ddnInteractions_" + counter).html("Chooser <span class='caret'/>")
                $("#ddnQuiz_" + counter).click ->
                    $("#ddnInteractions_" + counter).val("Quiz")
                    $("#ddnInteractions_" + counter).html("Quiz <span class='caret'/>")
                $("#ddnItem_" + counter).click ->
                    $("#ddnInteractions_" + counter).val("Item")
                    $("#ddnInteractions_" + counter).html("Item <span class='caret'/>")

        createItem = (counter) ->
            copyForm = document.getElementById("fgpNeu_" + $("#ddnInteractions_" + counter).val())
            interactionCounter = $("#btnCreateInteraction_" + counter).attr("interactionCounter")
            interactionCounter++
            $("#btnCreateInteraction_" + counter).attr("interactionCounter", interactionCounter)
            stuff = copyForm.cloneNode(true)
            stuff.id = stuff.id + "_" + interactionCounter
            stuff.style.display="block"
            document.getElementById("fstNeuesStorypointContent_"+counter).appendChild(stuff)
            
            $("#" + stuff.id).find("#lgdNeuesItemFieldset").attr("id", "lgdNeuesItemFieldset_" +interactionCounter)
            $("#" + stuff.id).find("#fstNeuesItemContent").attr("id", "fstNeuesItemContent_" +interactionCounter)
            $("#" + stuff.id).find("#btnItemControlGroup").attr("id", "btnItemControlGroup_" +interactionCounter)
            $("#" + stuff.id).find("#btnItemEinklappen").attr("id", "btnItemEinklappen_" +interactionCounter)
            $("#" + stuff.id).find("#btnItemDelete").attr("id", "btnItemDelete_" +interactionCounter)
            $("#" + stuff.id).find("#inItemID").attr("id", "inItemID_" +interactionCounter)
            $("#" + stuff.id).find("#inItemDescription").attr("id", "itemDescription_" +interactionCounter)
            $("#" + stuff.id).find("#itemFeatureRef").attr("id", "itemFeatureRef_" +interactionCounter)
            $("#" + stuff.id).find("#inItemIsCollected").attr("id", "inItemIsCollected_" +interactionCounter)
            $("#" + stuff.id).find("#lblinItemID").attr("id", "lblinItemID_" +interactionCounter)
            $("#" + stuff.id).find("#lblItemIsCollected").attr("id", "lblItemIsCollected_" +interactionCounter)
            $("#" + stuff.id).find("#lblItemDescription").attr("id", "lblItemDescription_" +interactionCounter)
            $("#" + stuff.id).find("#lblItemFeatureRef").attr("id", "lblItemStorypointRef_" +interactionCounter)
            
            $("#lblinItemID_" + interactionCounter).attr("for", "inItemID_"+interactionCounter)
            $("#lblItemIsCollected_" + interactionCounter).attr("for", "inItemIsCollected_"+interactionCounter)
            $("#lblItemDescription_" + interactionCounter).attr("for", "inItemDescription_"+interactionCounter)
            $("#lblItemStorypointRef_" + interactionCounter).attr("for", "itemFeatureRef_"+interactionCounter)
            $("#itemFeatureRef_" + interactionCounter).val("NeuesFeature_" + counter)
            
            $("#" + stuff.id).find("#btnSwitchDown").attr("id", "btnSwitchDown_" + interactionCounter)
            $("#" + stuff.id).find("#btnSwitchUp").attr("id", "btnSwitchUp_" + interactionCounter)
            btnSwitchDown("#btnSwitchDown_" + interactionCounter, "#" + stuff.id)
            btnSwitchUp("#btnSwitchUp_" + interactionCounter, "#" + stuff.id)
            # Click Event für btnItemDelete
            $("#btnItemDelete_" + interactionCounter).click ->
               $("#fgpNeu_Item_" + interactionCounter).remove() if (confirm('Möchten Sie das Item wirklich löschen?'))

            # Click Event für btnItemEinklappen
            btnEinklappen("#btnItemEinklappen_" + interactionCounter, "#fstNeuesItemContent_" + interactionCounter)
                    
            # ItemName Trigger
            $("#inItemID_"+interactionCounter).keyup ->
                text = "Item: " + $("#inItemID_"+interactionCounter).val()
                $("#lgdNeuesItemFieldset_"+interactionCounter).text(text)


        createQuiz = (counter) ->
            copyForm = document.getElementById("fgpNeu_" + $("#ddnInteractions_" + counter).val())
            interactionCounter = $("#btnCreateInteraction_" + counter).attr("interactionCounter")
            interactionCounter++
            $("#btnCreateInteraction_" + counter).attr("interactionCounter", interactionCounter)
            stuff = copyForm.cloneNode(true)
            stuff.id = stuff.id + "_" + interactionCounter
            stuff.style.display="block"
            document.getElementById("fstNeuesStorypointContent_"+counter).appendChild(stuff)
            
            $("#" + stuff.id).find("#lgdNeuesQuizFieldset").attr("id", "lgdNeuesQuizFieldset_" +interactionCounter)
            $("#" + stuff.id).find("#fstNeuesQuizContent").attr("id", "fstNeuesQuizContent_" +interactionCounter)
            $("#" + stuff.id).find("#btnQuizControlGroup").attr("id", "btnQuizControlGroup_" +interactionCounter)
            $("#" + stuff.id).find("#btnQuizEinklappen").attr("id", "btnQuizEinklappen_" +interactionCounter)
            $("#" + stuff.id).find("#btnQuizDelete").attr("id", "btnQuizDelete_" +interactionCounter)
            $("#" + stuff.id).find("#inQuizID").attr("id", "inQuizID_" +interactionCounter)
            $("#" + stuff.id).find("#inQuizStorypointRef").attr("id", "inQuizStorypointRef_" +interactionCounter)
            $("#" + stuff.id).find("#inQuizOnTrue").attr("id", "inQuizOnTrue_" +interactionCounter)
            $("#" + stuff.id).find("#inQuizOnFalse").attr("id", "inQuizOnFalse_" +interactionCounter)
            $("#" + stuff.id).find("#inQuizQuestion").attr("id", "inQuizQuestion_" +interactionCounter)
            $("#" + stuff.id).find("#btnQuizAnswer").attr("id", "btnQuizAnswer_" +interactionCounter)
            $("#" + stuff.id).find("#btnSwitchDown").attr("id", "btnSwitchDown_" + interactionCounter)
            $("#" + stuff.id).find("#btnSwitchUp").attr("id", "btnSwitchUp_" + interactionCounter)
            btnSwitchDown("#btnSwitchDown_" + interactionCounter, "#" + stuff.id)
            btnSwitchUp("#btnSwitchUp_" + interactionCounter, "#" + stuff.id)

            # Click Event für btnQuizDelete
            $("#btnQuizDelete_" + interactionCounter).click ->
               $("#fgpNeu_Quiz_" + interactionCounter).remove() if (confirm('Möchten Sie das Quiz wirklich löschen?'))
    
            btnEinklappen("#btnQuizEinklappen_" + interactionCounter, "#fstNeuesQuizContent_" + interactionCounter)
            # quizID Trigger
            $("#inQuizID_"+interactionCounter).keyup ->
                text = "Quiz: " + $("#inQuizID_"+interactionCounter).val()
                $("#lgdNeuesQuizFieldset_"+interactionCounter).text(text)
            
            # Click Event für btnQuizAnswer
            $("#btnQuizAnswer_" + interactionCounter).click ->
                quizAnswerCounter = $("#btnQuizAnswer_" + interactionCounter).attr("quizAnswerCounter")
                quizAnswerCounter++
                $("#btnQuizAnswer_" + interactionCounter).attr("quizAnswerCounter", quizAnswerCounter)
                copyAnswer = document.getElementById("fgpAnswer")
                answer = copyAnswer.cloneNode(true)
                answer.id = answer.id + "_" + quizAnswerCounter
                answer.style.display="block"
                document.getElementById("fstNeuesQuizContent_" + interactionCounter).appendChild(answer)
                $("#" + answer.id).find("#lblQuizAnswerID").attr("id", "lblQuizAnswerID_" +quizAnswerCounter)
                $("#" + answer.id).find("#inQuizAnswerID").attr("id", "inQuizAnswerID_" +quizAnswerCounter)
                $("#" + answer.id).find("#lblQuizAnswerText").attr("id", "lblQuizAnswerText_" +quizAnswerCounter)
                $("#" + answer.id).find("#inQuizAnswerText").attr("id", "inQuizAnswerText_" +quizAnswerCounter)
                $("#" + answer.id).find("#btnQuizAnswerDelete").attr("id", "btnQuizAnswerDelete_" + quizAnswerCounter)
                # Click Event für btnQuizDelete
                $("#btnQuizAnswerDelete_" + quizAnswerCounter).click ->
                    $("#" + answer.id).remove() if (confirm('Möchten Sie die Antwort wirklich löschen?'))

        createChooser = (counter) ->
                copyForm = document.getElementById("fgpNeu_" + $("#ddnInteractions_" + counter).val())
                interactionCounter = $("#btnCreateInteraction_" + counter).attr("interactionCounter")
                interactionCounter++
                $("#btnCreateInteraction_" + counter).attr("interactionCounter", interactionCounter)
                stuff = copyForm.cloneNode(true)
                stuff.id = stuff.id + "_" + interactionCounter
                stuff.style.display="block"
                document.getElementById("fstNeuesStorypointContent_"+counter).appendChild(stuff)
                $("#" + stuff.id).find("#lgdNeuerChooserFieldset").attr("id", "lgdNeuerChooserFieldset_" +interactionCounter)
                $("#" + stuff.id).find("#fstNeuerChooserContent").attr("id", "fstNeuerChooserContent_" +interactionCounter)
                $("#" + stuff.id).find("#bgpChooserControlGroup").attr("id", "bgpChooserControlGroup_" +interactionCounter)
                $("#" + stuff.id).find("#btnChooserEinklappen").attr("id", "btnChooserEinklappen_" +interactionCounter)
                $("#" + stuff.id).find("#btnChooserDelete").attr("id", "btnChooserDelete_" +interactionCounter)
                $("#" + stuff.id).find("#inChooserID").attr("id", "inChooserID_" +interactionCounter)
                $("#" + stuff.id).find("#inChooserStorypointRef").attr("id", "inChooserStorypointRef_" +interactionCounter)
                $("#" + stuff.id).find("#inChooserQuestion").attr("id", "inChooserQuestion_" +interactionCounter)
                $("#" + stuff.id).find("#btnChooserAnswer").attr("id", "btnChooserAnswer_" +interactionCounter)
                $("#" + stuff.id).find("#btnSwitchDown").attr("id", "btnSwitchDown_" + interactionCounter)
                $("#" + stuff.id).find("#btnSwitchUp").attr("id", "btnSwitchUp_" + interactionCounter)
                btnSwitchDown("#btnSwitchDown_" + interactionCounter, "#" + stuff.id)
                btnSwitchUp("#btnSwitchUp_" + interactionCounter, "#" + stuff.id)

                # Click Event für btnChooserDelete
                $("#btnChooserDelete_" + interactionCounter).click ->
                   $("#fgpNeu_Chooser_" + interactionCounter).remove() if (confirm('Möchten Sie den Chooser wirklich löschen?'))
        
                # Click Event für btnChooserEinklappen
                btnEinklappen("#btnChooserEinklappen_" + interactionCounter, "#fstNeuerChooserContent_" + interactionCounter)

                # ChooserName Trigger
                $("#inChooserID_"+interactionCounter).keyup ->
                    text = "Chooser: " + $("#inChooserID_"+interactionCounter).val()
                    $("#lgdNeuerChooserFieldset_"+interactionCounter).text(text)
                
                # Click Event für btnChooserAnswer
                $("#btnChooserAnswer_" + interactionCounter).click ->
                    ChooserAnswerCounter = $("#btnChooserAnswer_" + interactionCounter).attr("ChooserAnswerCounter")
                    ChooserAnswerCounter++
                    $("#btnChooserAnswer_" + interactionCounter).attr("ChooserAnswerCounter", ChooserAnswerCounter)
                    copyAnswer = document.getElementById("fgpChooserAnswer")
                    answer = copyAnswer.cloneNode(true)
                    answer.id = answer.id + "_" + ChooserAnswerCounter
                    answer.style.display="block"
                    document.getElementById("fstNeuerChooserContent_" + interactionCounter).appendChild(answer)
                    $("#" + answer.id).find("#lblChooserAnswerID").attr("id", "lblChooserAnswerID_" +ChooserAnswerCounter)
                    $("#" + answer.id).find("#inChooserAnswerID").attr("id", "inChooserAnswerID_" +ChooserAnswerCounter)
                    $("#" + answer.id).find("#lblChooserAnswerText").attr("id", "lblChooserAnswerText_" +ChooserAnswerCounter)
                    $("#" + answer.id).find("#inChooserAnswerText").attr("id", "inChooserAnswerText_" +ChooserAnswerCounter)
                    $("#" + answer.id).find("#lblChooserAnswerItemRef").attr("id", "lblChooserAnswerItemRef_" +ChooserAnswerCounter)
                    $("#" + answer.id).find("#inChooserAnswerItemRef").attr("id", "inChooserAnswerItemRef_" +ChooserAnswerCounter)
                    $("#" + answer.id).find("#lblChooserAnswerFeatureRef").attr("id", "lblChooserAnswerFeatureRef_" +ChooserAnswerCounter)
                    $("#" + answer.id).find("#inChooserAnswerStorypointRef").attr("id", "inChooserAnswerStorypointRef_" +ChooserAnswerCounter)
                    $("#" + answer.id).find("#btnChooserAnswerDelete").attr("id", "btnChooserAnswerDelete_" + ChooserAnswerCounter)

                    # Click Event für btnChooserDelete
                    $("#btnChooserAnswerDelete_" + ChooserAnswerCounter).click ->
                        $("#" + answer.id).remove() if (confirm('Möchten Sie die Antwort wirklich löschen?'))

        btnSwitchDown = (buttonID, currentObjID) ->
            $(buttonID).click ->
                nextObject = $(currentObjID).next()
                currentObject = $(currentObjID)
                if typeof nextObject.attr("id") == 'undefined' || nextObject.prop('tagName') != currentObject.prop('tagName')
                    currentObject.animate { opacity: 0.25 }, "fast"
                    currentObject.animate { opacity: 1 }, "fast"
                    currentObject.animate { opacity: 0.25 }, "fast"
                    currentObject.animate { opacity: 1 }, "fast"
                    return
                else
                    currentObject.animate {opacity: 0.25}, 400, () ->
                        nextObject.insertBefore("#" + currentObject.attr("id"))
                    currentObject.animate {opacity: 1}, 500
                return
            return

        btnSwitchUp = (buttonID, currentObjID) ->
            $(buttonID).click ->
                prevObject = $(currentObjID).prev()
                currentObject = $(currentObjID)
                if typeof prevObject.attr("id") == 'undefined' || prevObject.prop('tagName') != currentObject.prop('tagName')
                    currentObject.animate { opacity: 0.25 }, "fast"
                    currentObject.animate { opacity: 1 }, "fast"
                    currentObject.animate { opacity: 0.25 }, "fast"
                    currentObject.animate { opacity: 1 }, "fast"
                    return
                else
                    currentObject.animate {opacity: 0.25}, 400, () ->
                        prevObject.insertAfter("#" + currentObject.attr("id"))
                    currentObject.animate {opacity: 1}, 500
                return
            return

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
                  {
                    id: 6
                    label: 'Node 6'
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
                input = document.getElementById('inMapSearch')
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
                    $("#inLatLngLocation").val(place.geometry.location.A + ", " + place.geometry.location.F)
                    bounds.extend place.geometry.location
                    i++
                  map.fitBounds bounds
                  return

                $(window).resize ->
                  autocomplete = new google.maps.places.Autocomplete(input, { types: ['geocode'] })
                  google.maps.event.trigger map, 'resize'
                  
                  return
                google.maps.event.addListener map, 'click', (event) ->
                    lat = event.latLng.lat()
                    lng = event.latLng.lng()
                    $("#inMapSearch").val(lat + ", " + lng)
                    $("#inLatLngLocation").val(lat + ", " + lng)
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

