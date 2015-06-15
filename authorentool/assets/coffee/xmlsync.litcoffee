

        startXMLSynchro = (xmlString) ->

            xmlDoc = $.parseXML(xmlString)
            $xml = $(xmlDoc)
            
            ##########################################Story-Daten
            
            #alert $xml.find('gml:pos').text()
            
            $("#inTitel").val($xml.find('Title').text())
            $("#ttaDescription").text($xml.find('Description').text())
            $("#inAutor").val($xml.find('Author').text())
            $("#inSize").val($xml.find('Size').text())
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
            if radiusAttr == "me"
                $("#ddnradius").val("Meter")
                $("#ddnradius").html("Meter <span class='caret' />")
            
            #######################Bis hierhin wird alle ordentlich eingetragen
            ###########################################StorypointDaten
            $xml
                ####################Löschen ALler bisherigen Storypoints
            
            list = document.getElementById("fhlStorypoints");
            i = 1
            while i < list.length
                if typeof list.childNodes[i] != 'undefined'
                    list.removeChild(list.childNodes[i]) 
                i++
                ####################Alle Kanten und Knoten des Graphen löschen
            window.nodes = []
            window.edges = []
            window.duplicateEdges = []
            
                #####################Definiere einen Counter für die neuen Storypoints
            counter = 1
            
            
            ##############Array, damit man weiß welche Features es alle gibt.
            #featureArray = []

            ##############Hier schleife

            
            $xml.find('Feature').each ->
                
                #Array für die InteractionList für einen Storypoint
                interactionArray = []
                
                
                createNewStorypointX(counter)
                featureId = $(this).attr('id').split("_Feature")[0]
                $("#inStorypoint_"+counter).val(featureId)
                #featureArray.push(featureId)
                
                ############## video is wrong, cause images could be given too...
                shownObject = $(this).find('Href').attr('xlink:href')
                if typeof shownObject != 'undefined'
                    shownObject = shownObject.split("#")[1]
                    $('#inAsset_' + counter).val(shownObject)
                    
                position = $(this).find("pos").text()
                position = position.replace(" ",", ")
                $("#inAnchorPoint_" + counter).val(position)
                
                
                trackerID = $(this).find('anchorRef').attr('xlink:href')
                $xml.find('Trackable').each ->
                    if $(this).find('tracker').attr('xlink:href') == trackerID
                        $('#inAnchorImg_' + counter).val($(this).find('src').text())
                        
                interactionId = ""
                $(this).find('InteractionRef').each ->
                    interactionId = $(this).attr('xlink:href').split("#")[1]
                    console.log('InteraktionRef_ID: ' + interactionId)
                    interactionArray.push(interactionId)

                i = 0
                while i < interactionArray.length
                
                    saveTime = true
                    
                    
                    $xml.find('Quiz').each ->
                        #console.log($(this).attr('id'))
                        if $(this).attr('id') == interactionArray[i]
                            $("#ddnInteractions_" + counter).val("Quiz")
                            createQuiz(counter)

                            $("#inQuizID_"+window.interactioncounter).val($(this).attr('id'))
                            ontrue = $xml.find('OnTrue').attr("xlink:href").split("#")[1]
                            $("#btnSetQuizOnTrueReferences_"+window.interactioncounter).val(ontrue)
                            $("#btnSetQuizOnTrueReferences_"+window.interactioncounter).html(ontrue + " <span class='caret' />")
                            onfalse = $xml.find('OnFalse').attr("xlink:href").split("#")[1]
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
                            #console.log($(this).attr('id'))
                            if $(this).attr('id') == interactionArray[i]
                                $("#ddnInteractions_" + counter).val("Chooser")
                                createChooser(counter)
                                
                                $("#inChooserID_" + window.interactioncounter).val($(this).attr('id'))
                                $("#inChooserQuestion_" + window.interactioncounter).val($(this).find('Question').text())
                                
                                #################Antworten erstellen beim Chooser
                                
                                $(this).find('Answer').each ->
                                    #sadly createChooserAnswers need a stuff.id. Therefore: build it here
                                    copyForm = document.getElementById("fgpNeu_" + $("#ddnInteractions_" + counter).val())
                                    stuff = copyForm.cloneNode(true)
                                    stuff.id = stuff.id + "_" + window.interactioncounter
                                    createChooserAnswers(window.interactioncounter, stuff)
                                    # window.chooserAnswerCounter
                                    $("#inChooserAnswerID_" + window.chooserAnswerCounter).val($(this).attr('id'))
                                    $("#inChooserAnswerText_" + window.chooserAnswerCounter).val($(this).find('Text').text())
                                    itemRef = $(this).find('ItemRef').attr("xlink:href").split("#")[1]
                                    if itemRef != ""
                                        $("#btnSetChooserItemReferences_"+window.chooserAnswerCounter).val(itemRef)
                                        $("#btnSetChooserItemReferences_"+window.chooserAnswerCounter).html(itemRef + " <span class='caret' />")
                                    featureRef = $(this).find('FeatureRef').attr("xlink:href")
                                    if typeof featureRef != 'undefined'
                                        featureRef = featureRef.split("#")[1]
                                        
                                    if featureRef != ""
                                        $("#btnSetChooserStorypointReferences_"+window.chooserAnswerCounter).val(featureRef)
                                        $("#btnSetChooserStorypointReferences_"+window.chooserAnswerCounter).html(featureRef + " <span class='caret' />")
                                    
                                console.log('Chooser gefunden')
                                saveTime = false
                                
                    if saveTime
                        $xml.find('Item').each ->
                            #console.log($(this).attr('id'))
                            if $(this).attr('id') == interactionArray[i]
                                $("#ddnInteractions_" + counter).val("Item")
                                createItem(counter)
                                $("#inItemID_" + window.interactioncounter).val($(this).attr('id'))
                                $("#inItemDescription_" + window.interactioncounter).val($(this).find('Description').text())
                                collected = $(this).find('IsCollected').text().trim()
                                if collected == "true"
                                    document.getElementById("inItemIsCollected_" + window.interactioncounter).checked = true
                                
                                console.log('Item Gefunden')            
                    
                    i++

                counter++
                
                
            #Jetzt alle ddns clicken in den Dorpdown menues
            i = 1
            while i < counter
                
                ind = i + 10 
                
                buttonValue = $("#btnSetQuizOnTrueReferences_"+ ind).val()
                console.log( buttonValue )
                
                
                

                
                i++
                
            $("#btnCreateNewStorypoint").click()
            
            
            #Jetzt per EdgeStorypointFinder die Id des Storypoints herrausfinden und dann die Sachen dort eintragen.
            #Bei dependencyTag und bei den Interaktionen

            
            
            return

           
           
           
           
