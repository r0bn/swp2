        startSynchro = () ->
            xml = '<?xml version="1.0" encoding="UTF-8"?>\r\n'
            xml += '<arml xmlns="http://www.opengis.net/arml/2.0"\r\n'
            xml += '    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\r\n'
            xml += '    xmlns:xlink="http://www.w3.org/1999/xlink"\r\n'
            xml += '    xmlns:gml="http://www.opengis.net/gml/3.2"\r\n'
            xml += '    xsi:schemaLocation="http://www.opengis.net/arml/2.0 schema/ExARML.xsd">\r\n'
            xml = synchronizeStoryTag(xml)
            xml = synchronizeDependencyTag(xml)
            xml = synchronizeARMLTag(xml)
            xml += '</arml>'
            return xml
            
        synchronizeStoryTag = (xml) ->
            xml += '    <Story>\r\n'
            xml += '        <Revision>7</Revision>\r\n'
            xml += '        <Title>' + synchronizeInputHelper("inTitel") + '</Title>\r\n'
            xml += '        <Description>' + synchronizeInputHelper("ttaDescription") + '</Description>\r\n'
            xml += '        <Author>' + synchronizeInputHelper("inAutor") + '</Author>\r\n'
            size = synchronizeInputHelper("inSize")
            if $.isNumeric(size) == false
                size = 0
            xml += '        <Size uom="' + synchronizeInputHelper("ddnsize") + '">' + size + '</Size>\r\n'
            xml += '        <Location>\r\n'
            xml += '            <gml:Point gml:id="Location">\r\n'
            xml += '                <gml:pos>' + synchronizeGPSCalc("inLatLngLocation") + '</gml:pos>\r\n'
            xml += '            </gml:Point>\r\n'
            xml += '        </Location>\r\n'
            radius =  synchronizeInputHelper("inRadius")
            if $.isNumeric(radius) == false
                radius = 0
            if synchronizeInputHelper("ddnradius") == "Meter"
                xml += '        <Radius uom="m">' + radius + '</Radius>\r\n'
            else
                xml += '        <Radius uom="km">' + radius + '</Radius>\r\n'
            xml += '    </Story>\r\n'
            return xml

        synchronizeDependencyTag = (xml) ->
            xml += '    <Dependency>\r\n'
                # Zu jedem Storypoint wechseln und durchlaufen
            $('#fhlStorypoints').find('.form-horizontal').each ->
                id = $(this).attr("id")
                id = id.split("_")
                xml += '        <Storypoint id="'+ synchronizeInputHelper("inStorypoint_"+ id[1]).replace(" ", "") + '" >\r\n'
                xml += '            <FeatureRef xlink:href="#' + synchronizeInputHelper("inStorypoint_"+ id[1]).replace(" ", "") + '_Feature" />\r\n'
                xml += '            <Metadata>\r\n'
                xml += '                <Accessible>'+ $("#inInternet_" + id[1]).is( ":checked" ) + '</Accessible>\r\n'
                xml += '                <Internet>'+ $("#inInternet_" + id[1]).is( ":checked" )+ '</Internet>\r\n'
                xml += '            </Metadata>\r\n'
                xml = synchronizeDependecyContainers(xml, id[1])
                if $("#inEndOfStory_" + id[1]).is( ":checked" ) == true
                    xml += '            <EndOfStory>true</EndOfStory>\r\n'
                xml += '        </Storypoint>\r\n'
            xml += '    </Dependency>\r\n'
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
                            tmp_xml = '                    <StorypointRef xlink:href="#' + $("#" + ddns1ID).val().replace(" ", "") +  '"/>\r\n'
                            if typeof ddns2ID != 'undefined' && $("#" + ddns2ID).val() != "" && $("#" + ddns2ID).val() != 'Neue Ref setzen'
                                tmp_xml += '                    <StorypointRef xlink:href="#' + $("#" + ddns2ID).val().replace(" ", "") +  '"/>\r\n'
                                if typeof ddns3ID != 'undefined' && $("#" + ddns3ID).val() != "" && $("#" + ddns3ID).val() != 'Neue Ref setzen'
                                    tmp_xml += '                    <StorypointRef xlink:href="#' + $("#" + ddns3ID).val().replace(" ", "") +  '"/>\r\n'
                        tmp_item_xml = ""
                        ddns1ID = $("#btnSetStorypointItemReferences_" + storypointID + "_" + row + "_1").attr("id")
                        ddns2ID = $("#btnSetStorypointItemReferences_" + storypointID + "_" + row + "_2").attr("id")
                        ddns3ID = $("#btnSetStorypointItemReferences_" + storypointID + "_" + row + "_3").attr("id")
                        if typeof ddns1ID != 'undefined' && $("#" + ddns1ID).val() != "" && $("#" + ddns1ID).val() != 'Neue Ref setzen'
                            tmp_item_xml = '                    <ItemRef xlink:href="#' + $("#" + ddns1ID).val().replace(" ", "") +  '"/>\r\n'
                            if typeof ddns2ID != 'undefined' && $("#" + ddns2ID).val() != "" && $("#" + ddns2ID).val() != 'Neue Ref setzen'
                                tmp_item_xml += '                    <ItemRef xlink:href="#' + $("#" + ddns2ID).val().replace(" ", "") +  '"/>\r\n'
                                if typeof ddns3ID != 'undefined' && $("#" + ddns3ID).val() != "" && $("#" + ddns3ID).val() != 'Neue Ref setzen'
                                    tmp_item_xml += '                    <ItemRef xlink:href="#' + $("#" + ddns3ID).val().replace(" ", "") +  '"/>\r\n'
                        if tmp_xml != ""
                            dependencyXML += '            <Container>\r\n'
                            dependencyXML += '                <Storypointlist>\r\n'
                            dependencyXML += tmp_xml
                            dependencyXML += '                </Storypointlist>\r\n'
                            if tmp_item_xml == ""
                                dependencyXML += '                <Itemlist />\r\n'
                            else
                                dependencyXML += '                <Itemlist>\r\n'
                                dependencyXML += tmp_item_xml
                                dependencyXML += '                </Itemlist>\r\n'
                            dependencyXML += '            </Container>\r\n'
                        tmp_xml = ""
                        tmp_item_xml = ""
                        row++
                if dependencyXML == ""
                    dependencyXML += '            <Container>\r\n'
                    dependencyXML += '                <Storypointlist />\r\n'
                    dependencyXML += '                <Itemlist />\r\n'
                    dependencyXML += '            </Container>\r\n'
                xml += dependencyXML
                return xml    

        synchronizeARMLTag = (xml) ->
            xml += '    <ARElements>\r\n'
            xml = synchronizeFeatures(xml)
            xml = synchronizeInteractionTag(xml)
            xml += '    </ARElements>\r\n'
            return xml

        synchronizeFeatures = (xml) ->
            $('#fhlStorypoints').find('.form-horizontal').each ->
                id = $(this).attr("id")
                id = id.split("_")
                id = id[1]
                xml += '        <Feature id="'+ synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Feature" >\r\n'
                xml += '            <name>'+ synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '</name>\r\n'
                xml += '            <anchors>\r\n'
                xml += '                <Geometry>\r\n'
                xml += '                    <assets>\r\n'
                if $("#inAsset_"+ id).val() != ''
                    videoID = synchronizeInputHelper("inAsset_"+ id)
                    videoID = videoID.split(".")
                    if videoID[1] == 'mp4'
                        xml += '                        <Video id="' + synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Feature_' + videoID[0] + '">\r\n'
                        xml += '                            <Href xlink:href="' + synchronizeInputHelper("inAsset_"+ id) + '" />\r\n'
                        xml += '                        </Video>\r\n'
                    if videoID[1] != 'mp4'
                        xml += '                        <Image id="'+ synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Feature_' + videoID[0] + '">\r\n'
                        xml += '                            <href xlink:href="' + synchronizeInputHelper("inAsset_"+ id) + '" />\r\n'
                        xml += '                        </Image>\r\n'
                xml += '                    </assets>\r\n'
                xml += '                    <gml:Point gml:id="' + synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Pos">\r\n'
                xml += '                        <gml:pos>' + synchronizeGPSCalc("inAnchorPoint_" + id) + '</gml:pos>\r\n'
                xml += '                    </gml:Point>\r\n'
                xml += '                </Geometry>\r\n'
                if $("#inAnchorImg_" + id).val() != ''
                    xml += '                <anchorRef xlink:href="#' + synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Feature_ImageTracker" />\r\n'
                    
                xml += '                <InteractionList>\r\n'
                interactions = $("#fgpInteractions_"+id).next()
                while typeof interactions.attr("id") != "undefined"
                    interID = interactions.attr("id")
                    interID = interID.split("_")
                    if interID[1] == "Item"
                        xml += '                    <InteractionRef xlink:href="#' + synchronizeInputHelper("inItemID_"+ interID[2]).replace(" ", "") + '" />\r\n'
                    if interID[1] == "Quiz"
                        xml += '                    <InteractionRef xlink:href="#' + synchronizeInputHelper("inQuizID_"+ interID[2]).replace(" ", "") + '" />\r\n'
                    if interID[1] == "Chooser"
                        xml += '                    <InteractionRef xlink:href="#' + synchronizeInputHelper("inChooserID_"+ interID[2]).replace(" ", "") + '" />\r\n'   
                    interactions = interactions.next()
                xml += '                </InteractionList>\r\n'
                xml += '            </anchors>\r\n'
                xml += '        </Feature>\r\n'
                if $("#inAnchorImg_" + id).val() != ''
                    xml += '        <Tracker id="' + synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Feature_ImageTracker">\r\n'
                    xml += '              <uri xlink:href="http://opengeospatial.org/arml/tracker/genericImageTracker" />\r\n'
                    xml += '        </Tracker>\r\n'
                    xml += '        <Trackable id="'+synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Feature_Trackable">\r\n'
                    xml += '            <enabled>true</enabled>\r\n'
                    xml += '            <assets />\r\n'
                    xml += '            <config>\r\n'
                    xml += '                <tracker xlink:href="#' + synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Feature_ImageTracker" />\r\n'
                    xml += '                <src>' + synchronizeInputHelper("inAnchorImg_"+ id) + '</src>\r\n'
                    xml += '            </config>\r\n'
                    xml += '            <size>0.2</size>\r\n'
                    xml += '        </Trackable>\r\n'
            return xml

        synchronizeInteractionTag = (xml) ->
            xml += '        <Interactions>\r\n'
            xml = synchronizeQuizes(xml)
            xml = synchronizeChoosers(xml)
            xml = synchronizeItems(xml)

            
            xml += '        </Interactions>\r\n'
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
                        xml += '            <Item id="' + synchronizeInputHelper("inItemID_"+ interID[2]).replace(" ", "") + '">\r\n'
                        xml += '                <Description>' + synchronizeInputHelper("inItemDescription_"+ interID[2]) + '</Description>\r\n'
                        xml += '                <FeatureRef xlink:href="#'+ synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Feature" />\r\n'
                        xml += '                <IsCollected>'+ $("#inItemIsCollected_"+ interID[2]).is( ":checked" )+ '</IsCollected>\r\n'
                        xml += '            </Item>\r\n'
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
                        xml += '            <Quiz id="' + synchronizeInputHelper("inQuizID_"+ interID[2]).replace(" ", "") + '">\r\n'
                        xml += '                <FeatureRef xlink:href="#'+ synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Feature" />\r\n'
                        if $("#btnSetQuizOnTrueReferences_"+ interID[2]).val() != '' && $("#btnSetQuizOnTrueReferences_"+ interID[2]).val() != 'Neue Ref setzen'
                            xml += '                <OnTrue xlink:href="#'+ synchronizeInputHelper("btnSetQuizOnTrueReferences_"+ interID[2]).replace(" ", "") + '_Feature" />\r\n'
                        else
                            xml += '                <OnTrue xlink:href="#" />\r\n'
                        if $("#btnSetQuizOnFalseReferences_"+ interID[2]).val() != '' && $("#btnSetQuizOnFalseReferences_"+ interID[2]).val() != 'Neue Ref setzen'
                            xml += '                <OnFalse xlink:href="#'+ synchronizeInputHelper("btnSetQuizOnFalseReferences_"+ interID[2]).replace(" ", "") + '_Feature" />\r\n'
                        xml += '                <Question>' + synchronizeInputHelper("inQuizQuestion_"+ interID[2]) + '</Question>\r\n'
                        answer = $("#btnQuizAnswer_" + interID[2]).parent().parent().next()
                        while typeof answer.attr("id") != "undefined"
                            answer_id = answer.attr("id").split("_")
                            answer_id = answer_id[1]
                            xml += '                <Answer id="' + synchronizeInputHelper("inQuizAnswerID_"+answer_id).replace(" ", "") + '">\r\n'
                            xml += '                    <Text>' + synchronizeInputHelper("inQuizAnswerText_" +answer_id) + '</Text>\r\n'
                            if $("#ddnState_" + answer_id).val() == "Wahr"
                                xml += '                    <Status>true</Status>\r\n'
                            else
                                xml += '                    <Status>false</Status>\r\n'
                            xml += '                </Answer>\r\n'
                            answer = answer.next()
                        xml += '            </Quiz>\r\n'
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
                        xml += '            <Chooser id="' + synchronizeInputHelper("inChooserID_"+ interID[2]).replace(" ", "") + '">\r\n'
                        xml += '                <FeatureRef xlink:href="#'+ synchronizeInputHelper("inStorypoint_"+ id).replace(" ", "") + '_Feature" />\r\n'
                        xml += '                <Question>' + synchronizeInputHelper("inChooserQuestion_"+ interID[2]) + '</Question>\r\n'
                        answer = $("#btnChooserAnswer_" + interID[2]).parent().parent().next()
                        while typeof answer.attr("id") != "undefined"
                            answer_id = answer.attr("id").split("_")
                            answer_id = answer_id[1]
                            xml += '                <Answer id="' + synchronizeInputHelper("inChooserAnswerID_"+answer_id).replace(" ", "") + '">\r\n'
                            xml += '                    <Text>' + synchronizeInputHelper("inChooserAnswerText_" +answer_id) + '</Text>\r\n'
                            if $("#btnSetChooserItemReferences_"+ answer_id).val() != '' && $("#btnSetChooserItemReferences_"+ answer_id).val() != 'Neue Ref setzen'
                                xml += '                    <ItemRef xlink:href="#' + synchronizeInputHelper("btnSetChooserItemReferences_" +answer_id).replace(" ", "") + '" />\r\n'
                            if $("#btnSetChooserStorypointReferences_"+ answer_id).val() != '' && $("#btnSetChooserStorypointReferences_"+ answer_id).val() != 'Neue Ref setzen'
                                xml += '                    <FeatureRef xlink:href="#' + synchronizeInputHelper("btnSetChooserStorypointReferences_" +answer_id).replace(" ", "") + '" />\r\n'
                            xml += '                </Answer>\r\n'
                            answer = answer.next()
                        xml += '            </Chooser>\r\n'
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
