        lightMedienBox = () ->
            $medien = $('#medienLightbox')
            $('[data-target="#medienLightbox"]').on 'click', (event) ->
              $medienBib = $(this).find('medienBib')
              css = 
                'maxWidth': $(window).width() - 100
                'maxHeight': $(window).height() - 100
              $medien.find('.close').addClass 'hidden'
              $medienBib.css css
              return
            $medien.on 'shown.bs.modal', (e) ->
              $medienBib = $(this).find('medienBib')
              $medien.find('.modal-dialog').css 'width': $medienBib.width()
              $medien.find('.close').removeClass 'hidden'
              return

        addMarker = (location, markers) ->
            center = new google.maps.LatLng(location.A, location.F)
            marker = new google.maps.Marker({
                position: location,
                map: window.map
            });
            marker.set("markerOwner", window.mapCaller)
            markers.push(marker)
            if window.mapCaller == "btnMap" && $("#ddnradius").val() != "Einheit"
                rad = $("#inRadius").val()
                if $("#ddnradius").val() == "Einheit"
                    rad = 0
                else if $("#ddnradius").val() == "Kilometer"
                    rad = rad * 1000
                circle = new (google.maps.Circle)
                    center: center
                    map: window.map
                    radius: Math.ceil(rad)
                    strokeColor: 'red'
                    strokeOpacity: 0.8
                    strokeWeight: 2
                    fillColor: 'blue'
                    fillOpacity: 0.35
                    editable: true
                circle.set("markerOwner", window.mapCaller)
                markers.push(circle)

                google.maps.event.addListener circle, 'radius_changed', () ->
                                    rad = circle.getRadius()
                                    if $("#ddnradius").val() == "Meter"
                                        $("#inRadius").val(Math.ceil(rad))
                                    else if $("#ddnradius").val() == "Kilometer"
                                        $("#inRadius").val(Math.ceil(rad / 1000))
                google.maps.event.addListener circle, 'center_changed', () ->
                    marker.setPosition(circle.center)
                google.maps.event.addListener marker, 'position_changed', () ->
                    lat = marker.position.A
                    lng = marker.position.F
                    $("#inLatLngLocation").val(lat + ", " + lng)
                return
            return

        setAllMap = (map, markers) ->
          i = 0
          while i < markers.length
            markers[i].setMap map
            i++
          return

      

        googleMap = () ->

                mapOptions = 
                  zoom: 8
                  center: new (google.maps.LatLng)(48.7758459, 9.182932100000016)
                  scaleControl: true
                window.map = new (google.maps.Map)($('#gmeg_map_canvas')[0], mapOptions)
                input = document.getElementById('inMapSearch')
                window.map.controls[google.maps.ControlPosition.TOP_LEFT].push input
                searchBox = new (google.maps.places.SearchBox)(input)
                autocomplete = new google.maps.places.Autocomplete(input, { types: ['geocode'] })
                autocomplete.bindTo('bounds', window.map);
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
                      map: window.map
                      icon: image
                      title: place.name
                      position: place.geometry.location)
                    markers.push marker


                    $("#inLatLngLocation").val(place.geometry.location.A + ", " + place.geometry.location.F)
                    bounds.extend place.geometry.location
                    i++
                  window.map.fitBounds bounds
                  return

                $(window).resize ->

                  google.maps.event.trigger window.map, 'resize'

                  return
                google.maps.event.addListener window.map, 'click', (event) ->
                    lat = event.latLng.lat()
                    lng = event.latLng.lng()
                    currentGPSField = $("#" + window.mapCaller).attr("gpsField")
                    $("#" + currentGPSField).val(lat + ", " + lng)
                    i = 0
                    max = markers.length
                    deleted = []
                    while i < max
                        markers[i].setMap null
                        if markers[i].get("markerOwner") == window.mapCaller
                            deleted.push(i)
                        i++
                    i = deleted.length
                    i--
                    while i > -1
                        markers.splice(deleted[i], 1);
                        i--
                    addMarker(event.latLng, markers);
                    return
                lightBox(markers)

        lightBox = (markers) ->
                $lightbox = $('#lightbox')
                $('[data-target="#lightbox"]').on 'click', (event) ->
                  $mapDiv = $(this).find('gmeg_map_canvas')
                  css = 
                    'maxWidth': $(window).width() - 100
                    'maxHeight': $(window).height() - 100
                  $lightbox.find('.close').addClass 'hidden'
                  $mapDiv.css css
                  google.maps.event.trigger window.map, 'resize'
                  return
                $lightbox.on 'shown.bs.modal', (e) ->
                  $mapDiv = $(this).find('gmeg_map_canvas')
                  $lightbox.find('.modal-dialog').css 'width': $mapDiv.width()
                  $lightbox.find('.close').removeClass 'hidden'
                  i = 0
                  while i < markers.length
                       if markers[i].get("markerOwner") == window.mapCaller
                            markers[i].setMap window.map
                            markers[i].getMap().setCenter(markers[i].position);
                            if markers[i] instanceof google.maps.Circle
                                rad = $("#inRadius").val()
                                if $("#ddnradius").val() == "Einheit"
                                    rad = 0
                                else if $("#ddnradius").val() == "Kilometer"
                                    rad = rad * 1000
                                markers[i].set('radius', Math.ceil(rad))
                       i++
                  google.maps.event.trigger window.map, 'resize'
                  return
                $lightbox.on 'hide.bs.modal', (e) ->
                    setAllMap(null, markers)

        initSafeButton = () -> 
            $("#btnCreateNewStorypoint").click ->
                window.safeButtonCounter++
                #alert window.safeButtonCounter
                checkSafeButton()
                return


        checkSafeButton = () ->
            if window.safeButtonCounter > 0            
                $("#btnSaveStory").css("display","block")
            else
                $("#btnSaveStory").css("display","none")
            return


        initScrollbar = () ->
            $header = $('#colColumn2')
            headerPos = $header.position().top
            $win = $(window)
            scrollBottom = $win.scrollTop() + $win.height()
            $win.scroll ->
                #Hier scrollt man nach unten. Dabei gilt: immer 30px unter Top und fix
              if $win.scrollTop() >= headerPos
                #alert "nach unten" + $win.scrollTop()
                $header.css
                  'position': 'fixed'
                  'top': '30px'
                  'right': '20px'
                #Hier wird für jede Position wenn man nach oben Scrollt berechnet, wie die 
                #das Div sich anzupassen hat.
              i = 0
              while i <= headerPos
                if $win.scrollTop() == i
                    $header.css
                        'position': 'fixed'
                        'top': Math.abs(i-150)
                        'right':'20px'
                i++
              return


   
            
        initDropdownClicks = () ->
            #DropdownMenu Radius
            $("#ddnme").click -> 

                if $("#inRadius").val() != "" && $("#ddnradius").val() == "Kilometer"
                    $("#inRadius").val(Math.ceil($("#inRadius").val() * 1000))
                $("#ddnradius").val("Meter")
                $("#ddnradius").html("Meter <span class='caret' />")
            $("#ddnkm").click -> 
                if $("#inRadius").val() != "" && $("#ddnradius").val() == "Meter"
                    $("#inRadius").val(Math.ceil($("#inRadius").val() / 1000))
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


        inIDSetter = (objectInput, objectFieldset, text, alternativeText) ->
                    objectInput.val(objectInput.val().replace(/\s+/, "") )
                    if objectInput.val() != ""
                        text = text + objectInput.val()
                        objectFieldset.text(text)
                    else
                        objectFieldset.text(alternativeText)

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

        setIDs = (node, counter) ->
            node.children().each () ->
                if typeof $(this).attr("id") != "undefined"
                    newID =  $(this).attr("id") + "_" + counter
                    $(this).attr("id", newID)
                    if typeof $(this).attr("for") != "undefined"
                        newFor = $(this).attr("for") + "_" + counter
                        $(this).attr("for", newFor)
                setIDs($(this), counter)
            return
                
        createItem = (counter) ->
            copyForm = document.getElementById("fgpNeu_" + $("#ddnInteractions_" + counter).val())          
            window.interactioncounter++
            interactionCounter = window.interactioncounter
            stuff = copyForm.cloneNode(true)
            stuff.id = stuff.id + "_" + interactionCounter
            stuff.style.display="block"
            document.getElementById("fstNeuesStorypointContent_"+counter).appendChild(stuff)
            setIDs($("#" + stuff.id), interactionCounter)

            btnSwitchDown("#btnSwitchDown_" + interactionCounter, "#" + stuff.id)
            btnSwitchUp("#btnSwitchUp_" + interactionCounter, "#" + stuff.id)
            # Click Event für btnItemDelete
            $("#btnItemDelete_" + interactionCounter).click ->
               $("#fgpNeu_Item_" + interactionCounter).remove() if (confirm('Möchten Sie das Item wirklich löschen?'))

            # Click Event für btnItemEinklappen
            btnEinklappen("#btnItemEinklappen_" + interactionCounter, "#fstNeuesItemContent_" + interactionCounter)
            
            # ItemName Trigger
            $("#inItemID_"+interactionCounter).keyup ->
                inIDSetter($("#inItemID_"+interactionCounter), $("#lgdNeuesItemFieldset_"+interactionCounter), "Item: ", "Neues Item")

            # inItemStorypointRef Text setzen
            $("#inItemStorypointRef_" + interactionCounter).val($("#inStorypoint_" + counter).val())   
            # inItemStorypointRef Trigger
            $("#inStorypoint_" + counter).on 'input', () ->
                objectInput = $("#inItemStorypointRef_" + interactionCounter)
                objectInput.val($("#inStorypoint_" + counter).val())
                if typeof objectInput.val() != "undefined"
                    objectInput.val(objectInput.val().replace(/\s+/, "") )
            helpRek( $("#" + stuff.id))

        createQuiz = (counter) ->
            copyForm = document.getElementById("fgpNeu_" + $("#ddnInteractions_" + counter).val())
            window.interactioncounter++
            interactionCounter = window.interactioncounter
            stuff = copyForm.cloneNode(true)
            stuff.id = stuff.id + "_" + interactionCounter
            stuff.style.display="block"
            document.getElementById("fstNeuesStorypointContent_"+counter).appendChild(stuff)
            setIDs($("#" + stuff.id), interactionCounter)
            btnSwitchDown("#btnSwitchDown_" + interactionCounter, "#" + stuff.id)
            btnSwitchUp("#btnSwitchUp_" + interactionCounter, "#" + stuff.id)
            

            # Click Event für btnQuizDelete
            $("#btnQuizDelete_" + interactionCounter).click ->
               $("#fgpNeu_Quiz_" + interactionCounter).remove() if (confirm('Möchten Sie das Quiz wirklich löschen?'))

            # Dropdown Event for btnSetQuizOnTrueReferences
            $("#btnSetQuizOnTrueReferences_" + interactionCounter).click ->
                # für OnTrue
                createDropdownQuizOnTrue(interactionCounter, "#btnSetQuizOnTrueReferences_" + interactionCounter, "#" + stuff.id)

            # Dropdown Event for btnSetQuizOnFalseReferences
            $("#btnSetQuizOnFalseReferences_" + interactionCounter).click ->
                # für OnFalse
                createDropdownQuizOnFalse(interactionCounter, "#btnSetQuizOnFalseReferences_" + interactionCounter, "#" + stuff.id)

            btnEinklappen("#btnQuizEinklappen_" + interactionCounter, "#fstNeuesQuizContent_" + interactionCounter)
            # quizID Trigger
            $("#inQuizID_"+interactionCounter).keyup ->
                inIDSetter($("#inQuizID_"+interactionCounter), $("#lgdNeuesQuizFieldset_"+interactionCounter), "Quiz: ", "Neues Quiz")
             
            # inQuizStorypointRef Text setzen
            $("#inQuizStorypointRef_" + interactionCounter).val($("#inStorypoint_" + counter).val())   
            # inItemStorypointRef Trigger
            $("#inStorypoint_" + counter).on 'input', () ->
                objectInput = $("#inQuizStorypointRef_" + interactionCounter)
                objectInput.val($("#inStorypoint_" + counter).val())
                if typeof objectInput.val() != "undefined"
                    objectInput.val(objectInput.val().replace(/\s+/, "") )
            
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
                setIDs($("#" + answer.id), quizAnswerCounter)

                # Click event für ddnstate
                createQuizDropdown( quizAnswerCounter ) 

                # Click Event für btnQuizAnswerDelete
                $("#btnQuizAnswerDelete_" + quizAnswerCounter).click ->
                    $("#" + answer.id).remove() if (confirm('Möchten Sie die Antwort wirklich löschen?'))
                helpRek( $("#" + answer.id))
            helpRek( $("#" + stuff.id))

        createQuizDropdown = (counter) ->
            #DropdownMenu QuizStatus
            $("#ddnTrue_" + counter).click ->
                $("#ddnState_" + counter).val("Wahr")
                $("#ddnState_" + counter).html("Wahr <span class='caret' />")
            $("#ddnFalse_" + counter).click ->
                $("#ddnState_" + counter).val("Falsch")
                $("#ddnState_" + counter).html("Falsch <span class='caret' />")


        createChooser = (counter) ->
                copyForm = document.getElementById("fgpNeu_" + $("#ddnInteractions_" + counter).val())
                window.interactioncounter++
                interactionCounter = window.interactioncounter
                stuff = copyForm.cloneNode(true)
                stuff.id = stuff.id + "_" + interactionCounter
                stuff.style.display="block"
                document.getElementById("fstNeuesStorypointContent_"+counter).appendChild(stuff)
                setIDs($("#" + stuff.id), interactionCounter)
                btnSwitchDown("#btnSwitchDown_" + interactionCounter, "#" + stuff.id)
                btnSwitchUp("#btnSwitchUp_" + interactionCounter, "#" + stuff.id)

                # Click Event für btnChooserDelete
                $("#btnChooserDelete_" + interactionCounter).click ->
                   $("#fgpNeu_Chooser_" + interactionCounter).remove() if (confirm('Möchten Sie den Chooser wirklich löschen?'))
        
                # Click Event für btnChooserEinklappen
                btnEinklappen("#btnChooserEinklappen_" + interactionCounter, "#fstNeuerChooserContent_" + interactionCounter)

                # ChooserName Trigger
                $("#inChooserID_"+interactionCounter).keyup ->
                   inIDSetter($("#inChooserID_"+interactionCounter), $("#lgdNeuerChooserFieldset_"+interactionCounter), "Chooser: ", "Neuer Chooser")

                # inChooserStorypointRef Text setzen
                $("#inChooserStorypointRef_" + interactionCounter).val($("#inStorypoint_" + counter).val())   
                # inItemStorypointRef Trigger
                $("#inStorypoint_" + counter).on 'input', () ->
                    objectInput = $("#inChooserStorypointRef_" + interactionCounter)
                    objectInput.val($("#inStorypoint_" + counter).val())
                    if typeof objectInput.val() != "undefined"
                        objectInput.val(objectInput.val().replace(/\s+/, "") )

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
                    setIDs($("#" + answer.id), ChooserAnswerCounter)

                    # Click Event für btnChooserDelete
                    $("#btnChooserAnswerDelete_" + ChooserAnswerCounter).click ->
                        $("#" + answer.id).remove() if (confirm('Möchten Sie die Antwort wirklich löschen?'))
                    helpRek( $("#" + answer.id))
                helpRek( $("#" + stuff.id))

        btnSwitchDown = (buttonID, currentObjID) ->
            $(buttonID).click ->
                nextObject = $(currentObjID).next()
                currentObject = $(currentObjID)
                if typeof nextObject.attr("id") == 'undefined' || nextObject.prop('tagName') != currentObject.prop('tagName')
                    currentObject.effect("shake")
                    return
                else
                    currentObject.animate {opacity: 0.25}, 400, () ->
                        nextObject.insertBefore("#" + currentObject.attr("id"))
                    currentObject.animate {opacity: 1}, 500
                    return
                return
            return

        btnSwitchUp = (buttonID, currentObjID) ->
            $(buttonID).click ->
                prevObject = $(currentObjID).prev()
                currentObject = $(currentObjID)
                if typeof prevObject.attr("id") == 'undefined' || prevObject.prop('tagName') != currentObject.prop('tagName')
                    currentObject.effect("shake")
                    return
                else
                    currentObject.animate {opacity: 0.25}, 400, () ->
                        prevObject.insertAfter("#" + currentObject.attr("id"))
                    currentObject.animate {opacity: 1}, 500
                    return
                return
            return

        btnEinklappen = (button, content) ->
            # Click Event für btnQuizEinklappen
            $(button).click ->
                if $(content).is( ":hidden" )
                    $(content).show( "slow" )
                    $(button).find("span").addClass('glyphicon-resize-small')
                    $(button).find("span").removeClass('glyphicon-resize-full')
                    return
                else
                    $(content).slideUp("slow")
                    $(button).find("span").removeClass('glyphicon-resize-small')
                    $(button).find("span").addClass('glyphicon-resize-full')
                    return
            return


