        startSynchro = () ->
            xml = '<?xml version="1.0" encoding="UTF-8"?>\n'
            xml += '<arml xmlns="http://www.opengis.net/arml/2.0"\n'
            xml += '    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n'
            xml += '    xmlns:xlink="http://www.w3.org/1999/xlink"\n'
            xml += '    xmlns:gml="http://www.opengis.net/gml/3.2"\n'
            xml += '    xsi:schemaLocation="http://www.opengis.net/arml/2.0 schema/ExARML.xsd">\n'
            xml = synchronizeStoryTag(xml)
            xml = synchronizeDependencyTag(xml)
            xml = synchronizeARMLTag(xml)
            xml += '</arml>'
            return xml
            
        synchronizeStoryTag = (xml) ->
            xml += '    <Story>\n'
            xml += '        <Revision>7</Revision>\n'
            xml += '        <Title>' + synchronizeInputHelper("inTitel") + '</Title>\n'
            xml += '        <Description>' + synchronizeInputHelper("ttaDescription") + '</Description>\n'
            xml += '        <Author>' + synchronizeInputHelper("inAutor") + '</Author>\n'
            xml += '        <Size uom="' + synchronizeInputHelper("ddnsize") + '">' + synchronizeInputHelper("inSize") + '</Size>\n'
            xml += '        <Location>\n'
            xml += '            <gml:Point gml:id="Location">\n'
            xml += '                <gml:pos>' + synchronizeGPSCalc("inLatLngLocation") + '</gml:pos>\n'
            xml += '            </gml:Point>\n'
            xml += '        </Location>\n'
            xml += '        <Radius uom="' + synchronizeInputHelper("ddnradius") + '">' + synchronizeInputHelper("inRadius") + '</Radius>\n'
            xml += '    </Story>\n'
            return xml

        synchronizeDependencyTag = (xml) ->
            xml += '    <Dependency>\n'
                # Zu jedem Storypoint wechseln und durchlaufen
            $('#fhlStorypoints').find('.form-horizontal').each ->
                id = $(this).attr("id")
                id = id.split("_")
                xml += '        <Storypoint id="'+ synchronizeInputHelper("inStorypoint_"+ id[1]) + '" >\n'
                xml += '            <FeatureRef xlink:href="#' + synchronizeInputHelper("inStorypoint_"+ id[1]) + '_Feature" />\n'
                xml += '            <Metadata>\n'
                xml += '                <Accessible>'+ $("#inInternet_" + id[1]).is( ":checked" ) + '</Accessible>\n'
                xml += '                <Internet>'+ $("#inInternet_" + id[1]).is( ":checked" )+ '</Internet>\n'
                xml += '            </Metadata>\n'
                xml += '            <Container>\n'
                xml += '                <Storypointlist />\n'
                xml += '                <Itemlist />\n'
                xml += '            </Container>\n'
                if $("#inEndOfStory_" + id[1]).is( ":checked" ) == true
                    xml += '            <EndOfStory>true</EndOfStory>\n'
                xml += '        </Storypoint>\n'
            xml += '    </Dependency>\n'
            return xml
            
        synchronizeARMLTag = (xml) ->
            xml += '    <ARElements>\n'
            xml = synchronizeFeatures(xml)
            xml = synchronizeInteractionTag(xml)
            xml += '    </ARElements>\n'
            return xml
        synchronizeFeatures = (xml) ->
            $('#fhlStorypoints').find('.form-horizontal').each ->
                id = $(this).attr("id")
                id = id.split("_")
                id = id[1]
                xml += '        <Feature id="'+ synchronizeInputHelper("inStorypoint_"+ id) + '_Feature" >\n'
                xml += '            <name>'+ synchronizeInputHelper("inStorypoint_"+ id) + '</name>\n'
                xml += '            <anchors>\n'
                xml += '                <Geometry>\n'
                xml += '                    <assets>\n'
                xml += '                        <Video id="' + synchronizeInputHelper("inAsset_"+ id) + '">\n'
                xml += '                            <Href xlink:href="' + synchronizeInputHelper("inAsset_"+ id) + '.mp4" />\n'
                xml += '                        </Video>\n'
                xml += '                    </assets>\n'
                xml += '                    <gml:Point gml:id="' + synchronizeInputHelper("inStorypoint_"+ id) + '_GPS">\n'
                xml += '                        <gml:pos>' + synchronizeGPSCalc("inAnchorPoint_" + id) + '</gml:pos>\n'
                xml += '                    </gml:Point>\n'
                xml += '                </Geometry>\n'
                xml += '                <anchorRef xlink:href="#' + synchronizeInputHelper("inStorypoint_"+ id) + '_ImageTracker" />\n'
                xml += '                <InteractionList>\n'
                interactions = $("#fgpInteractions_"+id).next().next().next()
                while typeof interactions.attr("id") != "undefined"
                    interID = interactions.attr("id")
                    interID = interID.split("_")
                    if interID[1] == "Item"
                        xml += '                    <InteractionRef xlink:href="#' + synchronizeInputHelper("inItemID_"+ interID[2]) + '" />\n'
                    if interID[1] == "Quiz"
                        xml += '                    <InteractionRef xlink:href="#' + synchronizeInputHelper("inQuizID_"+ interID[2]) + '" />\n'
                    if interID[1] == "Chooser"
                        xml += '                    <InteractionRef xlink:href="#' + synchronizeInputHelper("inChooserID_"+ interID[2]) + '" />\n'   
                    interactions = interactions.next()
                xml += '                </InteractionList>\n'
                xml += '            </anchors>\n'
                xml += '        </Feature>\n'
                xml += '        <Tracker id="#' + synchronizeInputHelper("inStorypoint_"+ id) + '_ImageTracker">\n'
                xml += '              <uri xlink:href="http://opengeospatial.org/arml/tracker/genericImageTracker" />\n'
                xml += '        </Tracker>\n'
                
            return xml
        synchronizeInteractionTag = (xml) ->
            xml += '        <Interactions>\n'
            xml = synchronizeItems(xml)
            xml = synchronizeQuizes(xml)
            xml = synchronizeChoosers(xml)

            
            xml += '        </Interactions>\n'
            return xml
        synchronizeItems = (xml) ->
            $('#fhlStorypoints').find('.form-horizontal').each ->
                id = $(this).attr("id")
                id = id.split("_")
                id = id[1]
                interactions = $("#fgpInteractions_"+id).next()
                while typeof interactions.attr("id") != "undefined"
                    interID = interactions.attr("id")
                    interID = interID.split("_")
                    if interID[1] == "Item"
                        xml += '            <Item id="' + synchronizeInputHelper("inItemID_"+ interID[2]) + '">\n'
                        xml += '                <Description>' + synchronizeInputHelper("inItemDescription_"+ interID[2]) + '</Description>\n'
                        xml += '                <FeatureRef xlink:href="#'+ synchronizeInputHelper("inStorypoint_"+ id) + '_Feature" />\n'
                        xml += '                <IsCollected>'+ $("#inItemIsCollected_"+ interID[2]).is( ":checked" )+ '</IsCollected>\n'
                        xml += '            </Item>\n'
                    interactions = interactions.next()
            return xml
        synchronizeQuizes = (xml) ->
            $('#fhlStorypoints').find('.form-horizontal').each ->
                id = $(this).attr("id")
                id = id.split("_")
                id = id[1]
                interactions = $("#fgpInteractions_"+id).next().next().next()
                while typeof interactions.attr("id") != "undefined"
                    interID = interactions.attr("id")
                    interID = interID.split("_")
                    if interID[1] == "Quiz"
                        xml += '            <Quiz id="' + synchronizeInputHelper("inQuizID_"+ interID[2]) + '">\n'
                        xml += '                <FeatureRef xlink:href="#'+ synchronizeInputHelper("inStorypoint_"+ id) + '_Feature" />\n'
                        xml += '                <OnTrue xlink:href="#'+ synchronizeInputHelper("inStorypoint_"+ id) + '_Feature" />\n'
                        xml += '                <OnFalse xlink:href="#'+ synchronizeInputHelper("inStorypoint_"+ id) + '_Feature" />\n'
                        xml += '                <Question>' + synchronizeInputHelper("inQuizQuestion_"+ interID[2]) + '</Question>\n'

                                      #  <Answer id="Punkt_A_Quiz_Answer_1">
                                      #      <Text> Ja </Text>
                                      #      <Status> true </Status>
                                     #   </Answer>
                                    #   <Answer id="Punkt_A_Quiz_Answer_2">
                                    #        <Text> Nein </Text>
                                   #         <Status> false </Status>
                                    #    </Answer>
                        xml += '            </Quiz>\n'
                    interactions = interactions.next()
            return xml

        synchronizeChoosers = (xml) ->
        
              #  <Chooser id="Punkt_B_Chooser">
              #      <FeatureRef xlink:href="#Punkt_B_Feature" />
              #      <Question> Wohin willst du gehen? </Question>
              #      <Answer id="Punkt_B_Chooser_Answer_1">
              #          <Text> Kap Tormentoso </Text>
              #          <ItemRef xlink:href="#Punkt_B_E1" />
              #          <FeatureRef xlink:href="#Punkt_B_Feature" />
              #      </Answer>
              #      <Answer id="Punkt_B_Chooser_Answer_2" >
              #          <Text> OneTableClub </Text>
              #          <ItemRef xlink:href="#Punkt_B_E2" />
              #      </Answer>
              #  </Chooser>
            return xml
            
        synchronizeGPSCalc = (inputID) ->
            return $("#"+inputID).val().replace ",",""
            
        synchronizeInputHelper = (inputID) ->
            if $("#"+inputID).val() != ""
                return $("#"+inputID).val()
            else
                return $("#"+inputID).attr("placeholder")