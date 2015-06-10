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
            radius =  synchronizeInputHelper("inRadius")
            if $.isNumeric(radius) == false
                radius = 0
            xml += '        <Radius uom="' + synchronizeInputHelper("ddnradius") + '">' + radius + '</Radius>\n'
            xml += '    </Story>\n'
            return xml

        synchronizeDependencyTag = (xml) ->
            xml += '    <Dependency>\n'
                # Zu jedem Storypoint wechseln und durchlaufen
            $('#fhlStorypoints').find('.form-horizontal').each ->
                id = $(this).attr("id")
                id = id.split("_")
                xml += '        <Storypoint id="'+ synchronizeInputHelper("inStorypoint_"+ id[1]).replace(" ", "") + '" >\n'
                xml += '            <FeatureRef xlink:href="#' + synchronizeInputHelper("inStorypoint_"+ id[1]).replace(" ", "") + '_Feature" />\n'
                xml += '            <Metadata>\n'
                xml += '                <Accessible>'+ $("#inInternet_" + id[1]).is( ":checked" ) + '</Accessible>\n'
                xml += '                <Internet>'+ $("#inInternet_" + id[1]).is( ":checked" )+ '</Internet>\n'
                xml += '            </Metadata>\n'
                xml = synchronizeDependecyContainers(xml, id[1])
                if $("#inEndOfStory_" + id[1]).is( ":checked" ) == true
                    xml += '            <EndOfStory>true</EndOfStory>\n'
                xml += '        </Storypoint>\n'
            xml += '    </Dependency>\n'
            return xml
        synchronizeDependecyContainers = (xml, storypointID) ->
                dependencyXML = "";
                rowCounter = $("#btnCreateReferences_" + storypointID).attr("rowCounter")
                if typeof rowCounter != 'undefined'
                    row = 1
                    while row <= rowCounter
                        tmp_xml = ""
                        ddns1ID = $("#btnSetStorypointReferences_" + storypointID + "_" + row + "_1").attr("id")
                        ddns2ID = $("#btnSetStorypointReferences_" + storypointID + "_" + row + "_2").attr("id")
                        ddns3ID = $("#btnSetStorypointReferences_" + storypointID + "_" + row + "_3").attr("id")
                        if typeof ddns1ID != 'undefined' && $("#" + ddns1ID).val() != "" && $("#" + ddns1ID).val() != 'Neue Ref setzen'
                            tmp_xml = '                    <StorypointRef xlink:href="#' + $("#" + ddns1ID).val().replace(" ", "") +  '"/>\n'
                            if typeof ddns2ID != 'undefined' && $("#" + ddns2ID).val() != "" && $("#" + ddns2ID).val() != 'Neue Ref setzen'
                                tmp_xml += '                    <StorypointRef xlink:href="#' + $("#" + ddns2ID).val().replace(" ", "") +  '"/>\n'
                                if typeof ddns3ID != 'undefined' && $("#" + ddns3ID).val() != "" && $("#" + ddns3ID).val() != 'Neue Ref setzen'
                                    tmp_xml += '                    <StorypointRef xlink:href="#' + $("#" + ddns3ID).val().replace(" ", "") +  '"/>\n'
                        tmp_item_xml = ""
                        ddns1ID = $("#btnSetStorypointItemReferences_" + storypointID + "_" + row + "_1").attr("id")
                        ddns2ID = $("#btnSetStorypointItemReferences_" + storypointID + "_" + row + "_2").attr("id")
                        ddns3ID = $("#btnSetStorypointItemReferences_" + storypointID + "_" + row + "_3").attr("id")
                        if typeof ddns1ID != 'undefined' && $("#" + ddns1ID).val() != "" && $("#" + ddns1ID).val() != 'Neue Ref setzen'
                            tmp_item_xml = '                    <ItemRef xlink:href="#' + $("#" + ddns1ID).val().replace(" ", "") +  '"/>\n'
                            if typeof ddns2ID != 'undefined' && $("#" + ddns2ID).val() != "" && $("#" + ddns2ID).val() != 'Neue Ref setzen'
                                tmp_item_xml += '                    <ItemRef xlink:href="#' + $("#" + ddns2ID).val().replace(" ", "") +  '"/>\n'
                                if typeof ddns3ID != 'undefined' && $("#" + ddns3ID).val() != "" && $("#" + ddns3ID).val() != 'Neue Ref setzen'
                                    tmp_item_xml += '                    <ItemRef xlink:href="#' + $("#" + ddns3ID).val().replace(" ", "") +  '"/>\n'
                        dependencyXML += '            <Container>\n'
                        dependencyXML += '                <Storypointlist>\n'
                        dependencyXML += tmp_xml
                        dependencyXML += '                </Storypointlist>\n'
                        if tmp_item_xml == ""
                            dependencyXML += '                <Itemlist />\n'
                        else
                            dependencyXML += '                <Itemlist>\n'
                            dependencyXML += tmp_item_xml
                            dependencyXML += '                </Itemlist>\n'
                        dependencyXML += '            </Container>\n'
                        tmp_xml = ""
                        tmp_item_xml = ""
                        row++
                $('#fhlStorypoints').find('.form-horizontal').each ->
                    currentStorypointId = $(this).attr("id")
                    currentStorypointId = currentStorypointId.split("_")
                    currentStorypointId = currentStorypointId[1]
                    interactions = $("#fgpInteractions_"+currentStorypointId).next()
                    while typeof interactions.attr("id") != "undefined"
                        interID = interactions.attr("id")
                        interID = interID.split("_")
                        if interID[1] == "Quiz"
                            if $("#btnSetQuizOnTrueReferences_"+ interID[2]).val() == synchronizeInputHelper("inStorypoint_"+ storypointID)
                                dependencyXML += '            <Container>\n'
                                dependencyXML += '                <Storypointlist>\n'
                                dependencyXML += '                  <StorypointRef xlink:href="#' + synchronizeInputHelper("inStorypoint_"+ currentStorypointId).replace(" ", "") + '" />\n'
                                dependencyXML += '                </Storypointlist>\n'
                                dependencyXML += '                <Itemlist />\n'
                                dependencyXML += '            </Container>\n'   
                            if $("#btnSetQuizOnFalseReferences_"+ interID[2]).val() == synchronizeInputHelper("inStorypoint_"+ storypointID)
                                dependencyXML += '            <Container>\n'
                                dependencyXML += '                <Storypointlist>\n'
                                dependencyXML += '                  <StorypointRef xlink:href="#' + synchronizeInputHelper("inStorypoint_"+ currentStorypointId).replace(" ", "") + '" />\n'
                                dependencyXML += '                </Storypointlist>\n'
                                dependencyXML += '                <Itemlist />\n'
                                dependencyXML += '            </Container>\n'
                        if interID[1] == "Chooser"
                            answer = $("#btnChooserAnswer_" + interID[2]).parent().next()
                            while typeof answer.attr("id") != "undefined"
                                answer_id = answer.attr("id").split("_")
                                answer_id = answer_id[1]
                                if $("#btnSetChooserStorypointReferences_"+ answer_id).val() == synchronizeInputHelper("inStorypoint_"+ storypointID)
                                    dependencyXML += '            <Container>\n'
                                    dependencyXML += '                <Storypointlist>\n'
                                    dependencyXML += '                  <StorypointRef xlink:href="#' + synchronizeInputHelper("inStorypoint_"+ currentStorypointId).replace(" ", "") + '" />\n'
                                    dependencyXML += '                </Storypointlist>\n'
                                    if $("#btnSetChooserItemReferences_"+ answer_id).val() != '' 
                                        dependencyXML += '                <Itemlist>\n'
                                        dependencyXML += '                   <ItemRef xlink:href="#' + synchronizeInputHelper("btnSetChooserItemReferences_"+ answer_id).replace(" ", "") + '"/>\n'
                                        dependencyXML += '                </Itemlist>\n'
                                    else
                                        dependencyXML += '                <Itemlist />\n'
                                    dependencyXML += '            </Container>\n'
                                answer = answer.next()
                        interactions = interactions.next()
                if dependencyXML == ""
                    dependencyXML += '            <Container>\n'
                    dependencyXML += '                <Storypointlist />\n'
                    dependencyXML += '                <Itemlist />\n'
                    dependencyXML += '            </Container>\n'
                xml += dependencyXML
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
                xml += '        <Feature id="'+ synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Feature" >\n'
                xml += '            <name>'+ synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '</name>\n'
                xml += '            <anchors>\n'
                xml += '                <Geometry>\n'
                xml += '                    <assets>\n'
                if $("#inAsset_"+ id).val() != ''
                    videoID = synchronizeInputHelper("inAsset_"+ id)
                    videoID = videoID.split(".")
                    if videoID[1] == 'mp4'
                        xml += '                        <Video id="' + videoID[0] + '">\n'
                        xml += '                            <Href xlink:href="' + synchronizeInputHelper("inAsset_"+ id) + '" />\n'
                        xml += '                        </Video>\n'
                    if videoID[1] != 'mp4'
                        xml += '                        <Image id="' + videoID[0] + '">\n'
                        xml += '                            <Href xlink:href="' + synchronizeInputHelper("inAsset_"+ id) + '" />\n'
                        xml += '                        </Image>\n'
                xml += '                    </assets>\n'
                xml += '                    <gml:Point gml:id="' + synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Pos">\n'
                xml += '                        <gml:pos>' + synchronizeGPSCalc("inAnchorPoint_" + id) + '</gml:pos>\n'
                xml += '                    </gml:Point>\n'
                xml += '                </Geometry>\n'
                if $("#inAnchorImg_" + id).val() != ''
                    xml += '                <anchorRef xlink:href="#' + synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Feature_ImageTracker" />\n'
                    
                xml += '                <InteractionList>\n'
                interactions = $("#fgpInteractions_"+id).next().next().next()
                while typeof interactions.attr("id") != "undefined"
                    interID = interactions.attr("id")
                    interID = interID.split("_")
                    if interID[1] == "Item"
                        xml += '                    <InteractionRef xlink:href="#' + synchronizeInputHelper("inItemID_"+ interID[2]).replace(" ", "") + '" />\n'
                    if interID[1] == "Quiz"
                        xml += '                    <InteractionRef xlink:href="#' + synchronizeInputHelper("inQuizID_"+ interID[2]).replace(" ", "") + '" />\n'
                    if interID[1] == "Chooser"
                        xml += '                    <InteractionRef xlink:href="#' + synchronizeInputHelper("inChooserID_"+ interID[2]).replace(" ", "") + '" />\n'   
                    interactions = interactions.next()
                xml += '                </InteractionList>\n'
                xml += '            </anchors>\n'
                xml += '        </Feature>\n'
                if $("#inAnchorImg_" + id).val() != ''
                    xml += '        <Tracker id="#' + synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Feature_ImageTracker">\n'
                    xml += '              <uri xlink:href="http://opengeospatial.org/arml/tracker/genericImageTracker" />\n'
                    xml += '        </Tracker>\n'
                    xml += '        <Trackable id="#'+synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Feature_Trackable">\n'
                    xml += '            <enabled>true</enabled>\n'
                    xml += '            <assets />\n'
                    xml += '            <config>\n'
                    xml += '                <tracker xlink:href="#' + synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Feature_ImageTracker" />\n'
                    xml += '                <src>' + synchronizeInputHelper("inAnchorImg_"+ id) + '</src>\n'
                    xml += '            </config>\n'
                    xml += '            <size>0.2</size>\n'
                    xml += '        </Trackable>\n'
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
                        xml += '            <Item id="' + synchronizeInputHelper("inItemID_"+ interID[2]).replace(" ", "") + '">\n'
                        xml += '                <Description>' + synchronizeInputHelper("inItemDescription_"+ interID[2]) + '</Description>\n'
                        xml += '                <FeatureRef xlink:href="#'+ synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Feature" />\n'
                        xml += '                <IsCollected>'+ $("#inItemIsCollected_"+ interID[2]).is( ":checked" )+ '</IsCollected>\n'
                        xml += '            </Item>\n'
                    interactions = interactions.next()
            return xml
        synchronizeQuizes = (xml) ->
            $('#fhlStorypoints').find('.form-horizontal').each ->
                id = $(this).attr("id")
                id = id.split("_")
                id = id[1]
                interactions = $("#fgpInteractions_"+id).next()
                while typeof interactions.attr("id") != "undefined"
                    interID = interactions.attr("id")
                    interID = interID.split("_")
                    if interID[1] == "Quiz"
                        xml += '            <Quiz id="' + synchronizeInputHelper("inQuizID_"+ interID[2]).replace(" ", "") + '">\n'
                        xml += '                <FeatureRef xlink:href="#'+ synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Feature" />\n'
                        if $("#btnSetQuizOnTrueReferences_"+ interID[2]).val() != '' && $("#btnSetQuizOnTrueReferences_"+ interID[2]).val() != 'Neue Ref setzen'
                            xml += '                <OnTrue xlink:href="#'+ synchronizeInputHelper("btnSetQuizOnTrueReferences_"+ interID[2]).replace(" ", "") + '_Feature" />\n'
                        else
                            xml += '                <OnTrue xlink:href="#" />\n'
                        if $("#btnSetQuizOnFalseReferences_"+ interID[2]).val() != '' && $("#btnSetQuizOnFalseReferences_"+ interID[2]).val() != 'Neue Ref setzen'
                            xml += '                <OnFalse xlink:href="#'+ synchronizeInputHelper("btnSetQuizOnFalseReferences_"+ interID[2]).replace(" ", "") + '_Feature" />\n'
                        xml += '                <Question>' + synchronizeInputHelper("inQuizQuestion_"+ interID[2]) + '</Question>\n'
                        answer = $("#btnQuizAnswer_" + interID[2]).parent().next()
                        while typeof answer.attr("id") != "undefined"
                            answer_id = answer.attr("id").split("_")
                            answer_id = answer_id[1]
                            xml += '                <Answer id="' + synchronizeInputHelper("inQuizAnswerID_"+answer_id).replace(" ", "") + '">\n'
                            xml += '                    <Text>' + synchronizeInputHelper("inQuizAnswerText_" +answer_id) + '</Text>\n'
                            if $("#ddnState_" + answer_id).val() == "Wahr"
                                xml += '                    <Status>True</Status>\n'
                            else
                                xml += '                    <Status>False</Status>\n'
                            xml += '                </Answer>\n'
                            answer = answer.next()
                        xml += '            </Quiz>\n'
                    interactions = interactions.next()
            return xml

        synchronizeChoosers = (xml) ->
            $('#fhlStorypoints').find('.form-horizontal').each ->
                id = $(this).attr("id")
                id = id.split("_")
                id = id[1]
                interactions = $("#fgpInteractions_"+id).next()
                while typeof interactions.attr("id") != "undefined"
                    interID = interactions.attr("id")
                    interID = interID.split("_")
                    if interID[1] == "Chooser"
                        xml += '            <Chooser id="' + synchronizeInputHelper("inChooserID_"+ interID[2]).replace(" ", "") + '">\n'
                        xml += '                <FeatureRef xlink:href="#'+ synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Feature" />\n'
                        xml += '                <Question>' + synchronizeInputHelper("inChooserQuestion_"+ interID[2]) + '</Question>\n'
                        answer = $("#btnChooserAnswer_" + interID[2]).parent().next()
                        while typeof answer.attr("id") != "undefined"
                            answer_id = answer.attr("id").split("_")
                            answer_id = answer_id[1]
                            xml += '                <Answer id="' + synchronizeInputHelper("inChooserAnswerID_"+answer_id).replace(" ", "") + '">\n'
                            xml += '                    <Text>' + synchronizeInputHelper("inChooserAnswerText_" +answer_id) + '</Text>\n'
                            if $("#btnSetChooserItemReferences_"+ answer_id).val() != '' && $("#btnSetChooserItemReferences_"+ answer_id).val() != 'Neue Ref setzen'
                                xml += '                    <ItemRef>' + synchronizeInputHelper("btnSetChooserItemReferences_" +answer_id).replace(" ", "") + '</ItemRef>\n'
                            if $("#btnSetChooserStorypointReferences_"+ answer_id).val() != '' && $("#btnSetChooserStorypointReferences_"+ answer_id).val() != 'Neue Ref setzen'
                                xml += '                    <FeatureRef xlink:href="#' + synchronizeInputHelper("btnSetChooserStorypointReferences_" +answer_id).replace(" ", "") + '" />\n'
                            xml += '                </Answer>\n'
                            answer = answer.next()
                        xml += '            </Chooser>\n'
                    interactions = interactions.next()
            return xml
            
        synchronizeGPSCalc = (inputID) ->
            gps = $("#"+inputID).val()
            if gps == ''
                gps = "0.0 0.0"
            else
                tmp = gps.split(",")
                if tmp.length == 2
                    x = $.trim(tmp[0])
                    y = $.trim(tmp[1])
                    gps = parseFloat(x).toFixed(6) + " " + parseFloat(y).toFixed(6)
                else
                    gps = "0.0 0.0"
            return gps
            
        synchronizeInputHelper = (inputID) ->
            if $("#"+inputID).val() != ""
                return $("#"+inputID).val()
            else
                return $("#"+inputID).attr("placeholder")
