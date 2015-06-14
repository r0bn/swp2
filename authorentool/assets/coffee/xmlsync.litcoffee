

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
            
            
            ##############Hier schleife

            
            $xml.find('Feature').each ->
                createNewStorypointX(counter)
                $("#inStorypoint_"+counter).val($(this).attr('id').split("_Feature")[0])
                video = $(this).find('Href').attr('xlink:href')
                if typeof video != 'undefined'
                    video = video.split("#")
                    $('#inAsset_' + counter).val(video[1])
                $("#inAnchorPoint_" + counter).val($(this).find("pos").text())
                trackerID = $(this).find('anchorRef').attr('xlink:href')
                $xml.find('Trackable').each ->
                    if $(this).find('tracker').attr('xlink:href') == trackerID
                        $('#inAnchorImg_' + counter).val($(this).find('src').text())
                counter++
                return
            dependencyTag = xmlString.split("<Dependency>")[1].split("</Dependency>")[0]
            #$xml.find('Trackable').each ->
            
            
            
            return

           
           
           
           
