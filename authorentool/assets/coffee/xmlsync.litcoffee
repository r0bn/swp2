

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
            featureArray = []

            ##############Hier schleife

            
            $xml.find('Feature').each ->
                
                #Array für die InteractionList für einen Storypoint
                interactionArray = []
                
                
                createNewStorypointX(counter)
                featureId = $(this).attr('id').split("_Feature")[0]
                $("#inStorypoint_"+counter).val(featureId)
                featureArray.push(featureId)
                
                ############## video is wrong, cause images could be given too...
                shownObject = $(this).find('Href').attr('xlink:href')
                if typeof shownObject != 'undefined'
                    shownObject = shownObject.split("#")[1]
                    $('#inAsset_' + counter).val(shownObject)
                $("#inAnchorPoint_" + counter).val($(this).find("pos").text())
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
                            console.log('Quiz Gefunden')
                            saveTime = false
                            
                    if saveTime
                        $xml.find('Chooser').each ->
                            #console.log($(this).attr('id'))
                            if $(this).attr('id') == interactionArray[i]
                                $("#ddnInteractions_" + counter).val("Chooser")
                                createChooser(counter)
                                console.log('Chooser gefunden')
                                saveTime = false
                                
                    if saveTime
                        $xml.find('Item').each ->
                            #console.log($(this).attr('id'))
                            if $(this).attr('id') == interactionArray[i]
                                $("#ddnInteractions_" + counter).val("Item")
                                createItem(counter)
                                console.log('Item Gefunden')            
                    
                    i++
                    
                        
                counter++
                return
                
            
            
            #Jetzt per EdgeStorypointFinder die Id des Storypoints herrausfinden und dann die Sachen dort eintragen.
            #Bei dependencyTag und bei den Interaktionen
            
            $xml.find('Interaction').each ->
                
                
                
                
                
                
                
            
            dependencyTag = xmlString.split("<Dependency>")[1].split("</Dependency>")[0]
            
            
            return

           
           
           
           
