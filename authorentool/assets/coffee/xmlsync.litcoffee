

        startXMLSynchro = (xmlString) ->
            window.markers =[]
            xmlDoc = $.parseXML(xmlString)
            $xml = $(xmlDoc)
            
            ##########################################Story-Daten
            
            $("#inTitel").val($xml.find('Title').text())
            $("#ttaDescription").text($xml.find('Description:first').text())
            $("#inAutor").val($xml.find('Author').text())
            $("#ddnsize").val($xml.find('Size').attr("uom"))
            $("#ddnsize").html($xml.find('Size').attr("uom") + " <span class='caret' />")
            
            locationTag = xmlString.split("<Location>")[1].split("</Location>")[0]
            GPSTagLocation = locationTag.split("<gml:pos>")[1].split("</gml:pos>")[0]
            gps1 = GPSTagLocation.split(" ")[0]
            gps2 = GPSTagLocation.split(" ")[1]
            if typeof gps2 != 'undefined'
                $("#inLatLngLocation").val(gps1 + ", " +gps2)
            
            
            $("#inRadius").val($xml.find('Radius').text())
            radiusAttr = $xml.find('Radius').attr("uom")
            if radiusAttr == "km"
                $("#ddnradius").val("Kilometer")
                $("#ddnradius").html("Kilometer <span class='caret' />")
            if radiusAttr == "m"
                $("#ddnradius").val("Meter")
                $("#ddnradius").html("Meter <span class='caret' />")
            window.mapCaller = 'btnMap'
            latlong = new (google.maps.LatLng)(gps1,gps2)
            addMarker(latlong, window.markers)
            ###########################################StorypointDaten
            $xml
                ####################Löschen ALler bisherigen Storypoints

            $("#fhlStorypoints").children().each ->
                if typeof $(this).attr("id") != 'undefined' && $(this).attr("id") != "fgpStorypoint"
                    $(this).remove()
                    return
                ####################Alle Kanten und Knoten des Graphen löschen
            window.nodes = []
            window.edges = []
            window.duplicateEdges = []
            
                #####################Definiere einen Counter für die neuen Storypoints
            counter = 1

            
            $xml.find('Feature').each ->
                
                #Array für die InteractionList für einen Storypoint
                interactionArray = []
                
                
                createNewStorypointX(counter)
                
                featureId = $(this).attr('id').split("_Feature")[0]
                $("#inStorypoint_"+counter).val(featureId)
                $("#inStorypoint_"+counter).trigger("keyup")

                shownObject = $(this).find('Href').attr('xlink:href')
                if typeof shownObject == 'undefined'
                    shownObject = $(this).find('href').attr('xlink:href')
                if typeof shownObject != 'undefined'
                    $('#inAsset_' + counter).val(shownObject)
                    
                position = $(this).find("pos").text()
                position = position.replace(" ",", ")
                $("#inAnchorPoint_" + counter).val(position)
                position = position.split(", ")
                gps1 = position[0]
                gps2 = position[1]
                window.mapCaller = "btnStorypointMap_" + counter
                latlong = new (google.maps.LatLng)(gps1,gps2)
                addMarker(latlong, window.markers)
                setAllMap(null, markers)
                trackerID = $(this).find('anchorRef').attr('xlink:href')
                $xml.find('Trackable').each ->
                    if $(this).find('tracker').attr('xlink:href') == trackerID
                        $('#inAnchorImg_' + counter).val($(this).find('src').text())
                        
                interactionId = ""
                $(this).find('InteractionRef').each ->
                    interactionId = $(this).attr('xlink:href').split("#")[1]
                    interactionArray.push(interactionId)

                i = 0
                while i < interactionArray.length
                
                    saveTime = true

                    $xml.find('Quiz').each ->
                        if $(this).attr('id') == interactionArray[i]
                            $("#ddnInteractions_" + counter).val("Quiz")
                            createQuiz(counter)

                            $("#inQuizID_"+window.interactioncounter).val($(this).attr('id'))
                            $("#inQuizID_"+window.interactioncounter).trigger("keyup")
                            
                            
                            ontrue = $xml.find('OnTrue').attr("xlink:href")
                            if typeof ontrue != 'undefined'
                                ontrue = ontrue.split("#")[1]
                                ontrue = ontrue.split("_Feature")[0]
                                $("#btnSetQuizOnTrueReferences_"+window.interactioncounter).val(ontrue)
                                $("#btnSetQuizOnTrueReferences_"+window.interactioncounter).html(ontrue + " <span class='caret' />")
                            
                            #onfalse. Check if the button is defined
                            onfalse = $xml.find('OnFalse').attr("xlink:href")
                            if typeof onfalse != 'undefined'
                                onfalse = onfalse.split("#")[1]
                                onfalse = onfalse.split("_Feature")[0]
                                $("#btnSetQuizOnFalseReferences_"+window.interactioncounter).val(onfalse)
                                $("#btnSetQuizOnFalseReferences_"+window.interactioncounter).html(onfalse + " <span class='caret' />")
                 
                            $("#inQuizQuestion_" + window.interactioncounter).val($(this).find('Question').text())

                            
                            #############Antworten erstellen beim Quiz
                            $(this).find('Answer').each ->
                                createQuizAnswers(window.interactioncounter)
                                # window.quizAnswerCounter
                                $("#inQuizAnswerID_" + window.quizAnswerCounter).val($(this).attr('id'))
                                $("#inQuizAnswerText_" + window.quizAnswerCounter).val($(this).find('Text').text())
                                status = $(this).find('Status').text()
                                if status.trim() == "true"
                                    $("#ddnState_"+window.quizAnswerCounter).val("Wahr")
                                    $("#ddnState_"+window.quizAnswerCounter).html("Wahr <span class='caret' />")
                                else
                                    $("#ddnState_"+window.quizAnswerCounter).val("Falsch")
                                    $("#ddnState_"+window.quizAnswerCounter).html("Falsch <span class='caret' />")
                            
                            saveTime = false

                            
                            
                    if saveTime
                        $xml.find('Chooser').each ->
                            if $(this).attr('id') == interactionArray[i]
                                $("#ddnInteractions_" + counter).val("Chooser")
                                createChooser(counter)
                                
                                $("#inChooserID_" + window.interactioncounter).val($(this).attr('id'))
                                $("#inChooserID_" + window.interactioncounter).trigger("keyup")
                                $("#inChooserQuestion_" + window.interactioncounter).val($(this).find('Question').text())
                                
                                #################Antworten erstellen beim Chooser
                                
                                $(this).find('Answer').each ->
                                    copyForm = document.getElementById("fgpNeu_" + $("#ddnInteractions_" + counter).val())
                                    stuff = copyForm.cloneNode(true)
                                    stuff.id = stuff.id + "_" + window.interactioncounter
                                    createChooserAnswers(window.interactioncounter, stuff)
                                    # window.chooserAnswerCounter
                                    $("#inChooserAnswerID_" + window.chooserAnswerCounter).val($(this).attr('id'))
                                    $("#inChooserAnswerText_" + window.chooserAnswerCounter).val($(this).find('Text').text())

                                    itemRef = $(this).find('ItemRef').attr("xlink:href")
                                    if typeof itemRef != 'undefined'
                                        itemRef = itemRef.split("#")[1]
                                        $("#btnSetChooserItemReferences_"+window.chooserAnswerCounter).val(itemRef)
                                        $("#btnSetChooserItemReferences_"+window.chooserAnswerCounter).html(itemRef + " <span class='caret' />")
                                    
                                    ########FeatureRef Prüfen
                                    
                                    featureRef = $(this).find('FeatureRef').attr("xlink:href")
                                    if typeof featureRef != 'undefined'
                                        featureRef = featureRef.split("#")[1]
                                        featureRef = featureRef.split("_Feature")[0]
                                        
                                    if featureRef != ""
                                        $("#btnSetChooserStorypointReferences_"+window.chooserAnswerCounter).val(featureRef)
                                        $("#btnSetChooserStorypointReferences_"+window.chooserAnswerCounter).html(featureRef + " <span class='caret' />")

                                saveTime = false
                                
                    if saveTime
                        $xml.find('Item').each ->
                            if $(this).attr('id') == interactionArray[i]
                                $("#ddnInteractions_" + counter).val("Item")
                                createItem(counter)
                                $("#inItemID_" + window.interactioncounter).val($(this).attr('id'))
                                $("#inItemID_" + window.interactioncounter).trigger("keyup")
                                
                                $("#inItemDescription_" + window.interactioncounter).val($(this).find('Description').text())
                                collected = $(this).find('IsCollected').text().trim()
                                if collected == "true"
                                    document.getElementById("inItemIsCollected_" + window.interactioncounter).checked = true        
                    
                    i++

                counter++
 
 
 
            # Add Edges to the graph. Interactions (Chooser and Quiz) ONLY!!!


            ind = 10 
            while ind <= window.interactioncounter
                #Beim Item werden keine Kanten gesetzt. Daher ist das egal.
                if typeof $("#fgpNeu_Item_"+ind).attr("id") != 'undefined'
                    
                    ind++
                    continue
                    
                #Quiz Kanten setzten: 
                
                if typeof $("#fgpNeu_Quiz_"+ind).attr("id") != 'undefined'
                    #OnTrue Kanten setzten
                    buttonOnTrueValue = $("#btnSetQuizOnTrueReferences_"+ ind).val().split("_Feature")[0]
                    if typeof buttonOnTrueValue != 'undefined'
                    
                        toStorypointId = findStorypointByButtonValue( buttonOnTrueValue, "#btnSetQuizOnTrueReferences_"+ ind )
                        previousStorypoint = edgeStorypointfinder("#btnSetQuizOnTrueReferences_" + ind, "fhlNeuerStorypoint" )
                        
                        previousStorypoint = previousStorypoint.split("_")
                        previousStorypointId = previousStorypoint[previousStorypoint.length - 1]
                        
                        if toStorypointId != -1
                            AddEdge(previousStorypointId, toStorypointId)


                    #OnFalse Kanten setzten
                    buttonOnFalseValue = $("#btnSetQuizOnFalseReferences_"+ ind).val().split("_Feature")[0]

                    if typeof buttonOnFalseValue != 'undefined'
                    
                        toStorypointId = findStorypointByButtonValue( buttonOnFalseValue, "#btnSetQuizOnFalseReferences_"+ ind )
                        previousStorypoint = edgeStorypointfinder("#btnSetQuizOnFalseReferences_" + ind, "fhlNeuerStorypoint" )
                        previousStorypoint = previousStorypoint.split("_")
                        previousStorypointId = previousStorypoint[previousStorypoint.length - 1]


                        if toStorypointId != -1
                            AddEdge(previousStorypointId, toStorypointId)


                #Chooser Kanten setzten:
                

                #if a chooser exists:
                if typeof $("#fgpNeu_Chooser_"+ind).attr("id") != 'undefined'
                
                    #Storypoint Kanten setzten
                    
                    answerCounter = 10
                    while answerCounter <= window.chooserAnswerCounter
                        buttonStorypointValue = $("#btnSetChooserStorypointReferences_"+ answerCounter).val()
                        
                        if typeof buttonStorypointValue == 'undefined'
                            answerCounter++
                            continue
                            
                        
                        
                        else
                        
                            buttonStorypointValue = buttonStorypointValue.split("_Feature")[0]
                            
                            toStorypointId = findStorypointByButtonValue( buttonStorypointValue, "#btnSetChooserStorypointReferences_"+ answerCounter )
                            
                            previousStorypoint = edgeStorypointfinder("#btnSetChooserStorypointReferences_" + answerCounter, "fhlNeuerStorypoint" )
                            previousStorypoint = previousStorypoint.split("_")
                            previousStorypointId = previousStorypoint[previousStorypoint.length - 1]

                            $("#btnSetChooserStorypointReferences_"+answerCounter).attr("oldValue", "fhlNeuerStorypoint_" + toStorypointId)
                            $("#btnSetChooserStorypointReferences_"+answerCounter).attr("currentEdge", "fhlNeuerStorypoint_" + toStorypointId)

                            
                            buttonItemValue = $("#btnSetChooserItemReferences_"+ answerCounter).val()

                            if toStorypointId != -1
                                addItemToEdge(previousStorypointId, toStorypointId, buttonItemValue)


                        answerCounter++

                ind++


            window.safeButtonCounter = counter
            checkSafeButton()
            
            
            $xml.find('Dependency').each ->
                $(this).find('Storypoint').each ->
                    feature = $(this).find('FeatureRef').attr('xlink:href')
                    featureID = feature.split('#')[1]
                    if typeof featureID != 'undefined'
                        featureID = featureID.split("_Feature")[0]
                        access = $(this).find('Accessible').text()
                        internet = $(this).find('Internet').text()
                        endOfStory = $(this).find('EndOfStory').text()
                        i = 1
                        storypointID = 0
                        while i < counter
                            if $('#inStorypoint_' + i).val() == featureID
                                storypointID = i
                                if internet == "true"
                                    document.getElementById('inInternet_' + i).checked = true
                                if access == "true"
                                    document.getElementById('inAccessable_' + i).checked = true
                                if typeof endOfStory != 'undefined'
                                    if endOfStory.trim() == "true"
                                       document.getElementById('inEndOfStory_' + i).checked = true
                            i++
                            
                        rowCounter = 0
                        
                        $(this).find('Container').each ->
                            rowCounter++
                            columnCounter = 1
                            $(this).find('Storypointlist').each ->
                                if $(this).find('StorypointRef:first').length != 0
                                    $('#btnCreateReferences_' + storypointID).click()
                                    
                                    $(this).find('StorypointRef').each ->
                                        reference = $(this).attr("xlink:href").split('#')[1].split('_Feature')[0]
                                        test = $('#ulStorypointRef_'+storypointID + '_' +rowCounter + '_' +columnCounter).children()
                                        z = 0
                                        while z < test.length
                                            if $(test[z]).children().text() == reference
                                                $(test[z]).children().click()
                                                break
                                            z++
                                        columnCounter++
                                        return
                                    return
                                return
                            columnCounter = 1
                            $(this).find('Itemlist').each ->
                                $(this).find('ItemRef').each ->
                                    reference = $(this).attr("xlink:href").split('#')[1]
                                    test = $('#ulSkStorypointItemRef_'+storypointID + '_' +rowCounter + '_' +columnCounter).children()
                                    z = 0
                                    while z < test.length
                                        if $(test[z]).children().text() == reference
                                            $(test[z]).children().click()
                                            break
                                        z++
                                    columnCounter++
                                    return
                                return
                            return
                        return
                    return
                return

            return




           
        #returns the Id of the storypoint by a given button Value
        findStorypointByButtonValue = ( buttonValue, currentStorypointId ) ->
            
            
            currentStorypointID = $(currentStorypointId).closest(".form-horizontal").attr("id")
            storypointArray = getAllStorypoints(0, "#"+currentStorypointID)

            i = 0
            while i < storypointArray.length
                
                currStorypointName = $("#inStorypoint_" + storypointArray[i].split("_")[1]).val()


                if buttonValue == currStorypointName                
                    returnStorypointID = $("#inStorypoint_" + storypointArray[i].split("_")[1]).attr("id").split("_")[1]
                    return returnStorypointID
                i++

            return -1
            
           
