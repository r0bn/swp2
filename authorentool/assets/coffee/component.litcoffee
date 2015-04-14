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
        $scope.handleStorySelected = () ->
            $scope.storySelected = true
        $scope.storys = [
            {
                name : "story01"
                author : "fritz"
                timestamp : "17. Okt 14"
            },
            {
                name : "story02"
                author : "hans"
                timestamp : "17. Dez 15"
            },
            {
                name : "kneipentour"
                author : "hugo"
                timestamp : "4. April 15"
            }
        ]

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

        $scope.xmlFile = """
        <?xml version="1.0" encoding="UTF-8"?> 
            <arml xmlns="http://www.opengis.net/arml/2.0" 
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xmlns:xlink="http://www.w3.org/1999/xlink"
              xmlns:gml="http://www.opengis.net/gml/3.2"
              xsi:schemaLocation="http://www.opengis.net/arml/2.0 schema/ExARML.xsd">
              
            <Story>
                <Title>Kneipentour</Title>
                <Description>Die Legände von Block 4</Description>
                <Author>Arno Claus</Author>
                <CreationDate>2015-04-01</CreationDate> <!-- Format: yyyy-mm-dd -->
                <Size>40 MB</Size>
                <Location>
                    <gml:Point gml:id="Location">
                        <gml:pos>48.780332 9.172515</gml:pos>
                    </gml:Point>
                </Location><!-- GML POINT -->
                <Radius>5</Radius><!-- ALS KILOMETER-->
                
                <POI id="Punkt_A">
                    <Accessible>true</Accessible>
                    <Internet>true</Internet>
                </POI>
                <POI id="Punkt_B">
                    <Accessible>true</Accessible>
                    <Internet>true</Internet>
                </POI>
                <POI id="Punkt_C_1">
                    <Accessible>true</Accessible>
                    <Internet>true</Internet>
                </POI>
                <POI id="Punkt_C_2">
                    <Accessible>true</Accessible>
                    <Internet>true</Internet>
                </POI>
                <POI id="Punkt_D_1">
                    <Accessible>true</Accessible>
                    <Internet>true</Internet>
                </POI>
                <POI id="Punkt_D_2">
                    <Accessible>true</Accessible>
                    <Internet>true</Internet>
                </POI>
                <POI id="Punkt_E">
                    <Accessible>true</Accessible>
                    <Internet>true</Internet>
                </POI>
            </Story>
            <Dependency>
                <Storypoint id="A" >
                    <Container>
                        <Storypointlist />
                        <Itemlist />
                    </Container>
                </Storypoint>
                <Storypoint id="B" >
                    <Container>
                        <Storypointlist>
                            <StorypointRef xlink:href="#A" />
                        </Storypointlist>
                        <Itemlist />
                    </Container>
                </Storypoint>
                <Storypoint id="C1" >
                    <Container>
                        <Storypointlist>
                            <StorypointRef xlink:href="#B" />
                        </Storypointlist>
                        <Itemlist>
                            <ItemRef xlink:href="Punkt_B_E1" />
                        </Itemlist>
                    </Container>
                </Storypoint>
                <Storypoint id="C2" >
                    <Container>
                        <Storypointlist>
                            <StorypointRef xlink:href="#B" />
                        </Storypointlist>
                        <Itemlist>
                            <ItemRef xlink:href="Punkt_B_E2" />
                        </Itemlist>
                    </Container>
                </Storypoint>
                <Storypoint id="D1">
                    <Container>
                        <Storypointlist>
                            <StorypointRef xlink:href="C1" />
                        </Storypointlist>
                        <Itemlist>
                            <ItemRef xlink:href="Punkt_B_E1" />
                        </Itemlist>
                    </Container>
                </Storypoint>
                <Storypoint id="D2">
                    <Container>
                        <Storypointlist>
                            <StorypointRef xlink:href="C2" />
                        </Storypointlist>
                        <Itemlist>
                            <ItemRef xlink:href="Punkt_B_E2" />
                        </Itemlist>
                    </Container>
                </Storypoint>
                <Storypoint id="E">
                    <Container>
                        <Storypointlist>
                            <StorypointRef xlink:href="D1" />
                        </Storypointlist>
                    </Container>
                    <Container>
                        <Storypointlist>
                            <StorypointRef xlink:href="D2" />
                        </Storypointlist>
                    </Container>
                </Storypoint>
            </Dependency>
            <ARElements>
                <Feature id="Punkt_A_Feature">
                    <name> Startpunkt </name>
                    <anchors>
                        <Geometry>
                            <assets>
                                <Video id="Anfangsvideo">
                                    <Href xlink:href="http://server.de/Anfangsvideo" />
                                </Video>
                            </assets>
                            <gml:Point gml:id="Punkt_A_Block4">
                                <gml:pos>48.780332 9.172515</gml:pos>
                            </gml:Point>
                        </Geometry>
                        <InteractionList>
                            <InteractionRef xlink:href="Punkt_A_Quiz" />
                        </InteractionList>
                    </anchors>
                </Feature>

                <Feature id="Punkt_A_1_Feature">
                    <name> Entscheidung 1 </name>
                    <anchors>
                        <Geometry>
                            <assets>
                                <Video id="Anfangsvideo_E1"> <!-- bei ja -->
                                    <Href xlink:href="http://server.de/Anfangsvideo_E1" />
                                </Video>
                            </assets>
                            <gml:Point gml:id="Punkt_A_Block4_1">
                                <gml:pos>48.780332 9.172515</gml:pos>
                            </gml:Point>
                        </Geometry>
                    </anchors>
                </Feature>
                
                <Feature id="Punkt_A_2_Feature">
                    <name> Entscheidung 2 </name>
                    <anchors>
                        <Geometry>
                            <assets>
                                <Video id="Anfangsvideo_E2"> <!-- bei nein -->
                                    <Href xlink:href="http://server.de/Anfangsvideo_E2" />
                                </Video>
                            </assets>
                            <gml:Point gml:id="Punkt_A_Block4_2">
                                <gml:pos>48.780332 9.172515</gml:pos>
                            </gml:Point>
                        </Geometry>
                    </anchors>
                </Feature>
                
                <Feature id="Punkt_B_Feature">
                    <name> Startpunkt </name>
                    <anchors>
                        <Geometry>
                            <assets>
                                <Video id="Studentenvideo">
                                    <Href xlink:href="http://server.de/Studentenvideo" />
                                </Video>
                            </assets>
                            <gml:Point gml:id="Punkt_B_PdR">
                                <gml:pos>48.779714 9.174050</gml:pos>
                            </gml:Point>
                        </Geometry>
                        <InteractionList>
                            <InteractionRef xlink:href="Punkt_B_Waychooser" />
                        </InteractionList>
                    </anchors>
                </Feature>
                
                <!-- to the end now ... -->
                
                <Feature id="Punkt_C_1_Feature">
                    <name> Kap Tormentoso </name>
                    <anchors>
                        <Geometry>
                            <assets>
                                <!-- Wie mache ich ein Image genau? -->
                                <Image id="Piratenzeugs">
                                    <href xlink:href="http://server.de/Piratenzeugs" />
                                </Image>
                            </assets>
                            <gml:Point gml:id="Punkt_C_1_KT">
                                <gml:pos>48.779782 9.174015</gml:pos>
                            </gml:Point>
                        </Geometry>
                        <InteractionList>
                            <InteractionRef xlink:href="Punkt_C_1_Item" />
                        </InteractionList>
                    </anchors>
                </Feature>
                
                <Feature id="Punkt_C_2_Feature">
                    <name> OneTableClub </name>
                    <anchors>
                        <Geometry>
                            <assets>
                                <Image id="gebrauchter_Rucksack">
                                    <href xlink:href="http://server.de/Rucksack" />
                                </Image>
                            </assets>
                            <gml:Point gml:id="Punkt_C_1_OTC">
                                <gml:pos>48.775659 9.171824</gml:pos>
                            </gml:Point>
                        </Geometry>
                        <InteractionList>
                            <InteractionRef xlink:href="Punkt_C_2_Item" />
                        </InteractionList>
                    </anchors>
                </Feature>
                
                <Feature id="Punkt_D_1_Feature">
                    <name> Pirat </name>
                    <anchors>
                        <Geometry>
                            <assets>
                                <Video id="Piratengespraech">
                                    <Href xlink:href="http://server.de/Piratengespraech" />
                                </Video>
                            </assets>
                            <gml:Point gml:id="Punkt_D_1_Pirat">
                                <gml:pos>48.779714 9.174050</gml:pos>
                            </gml:Point>
                        </Geometry>
                    </anchors>
                </Feature>
                
                <Feature id="Punkt_D_1_Feature1">
                    <name> Student </name>
                    <anchors>
                        <Geometry>
                            <assets>
                                <Video id="Studentengespraech">
                                    <Href xlink:href="http://server.de/Studentengespraech" />
                                </Video>
                            </assets>
                            <gml:Point gml:id="Punkt_D_1_Student">
                                <gml:pos>48.779714 9.174050</gml:pos>
                            </gml:Point>
                        </Geometry>
                    </anchors>
                </Feature>
                
                <Feature>
                    <name>Ende</name>
                    <anchors>
                        <Geometry>
                            <assets>
                                <Video id="Ende">
                                    <Href xlink:href="http://server.de/Ende" />
                                </Video>
                            </assets>
                            <gml:Point gml:id="Punkt_E_1" >
                                <gml:pos>48.780332 9.172515</gml:pos>
                            </gml:Point>
                        </Geometry>
                    </anchors>
                </Feature>
            
                <Interactions>
                    <Quiz id="Punkt_A_Quiz">
                        <FeatureRef xlink:href="#Punkt_A_Feature" />
                        <OnTrue xlink:href="Feature_A_1"/>
                        <OnFalse xlink:href="Feature_A_2" />
                        <Question> Bist du der Auserwählte? </Question>
                                        
                        <Answer id="A1">
                            <Text> Ja </Text>
                            <Status> true </Status>
                        </Answer>
                        <Answer id="A2">
                            <Text> Nein </Text>
                            <Status> false </Status>
                        </Answer>
                        
                    </Quiz>
                    <!-- waychooser musste erst erfunden werden am 1.4. April april -->
                    <WayChooser id="Punkt_B_Waychooser">
                        <FeatureRef xlink:href="#Punkt_B_Feature" />
                        <Question> Wohin willst du gehen? </Question>
                        <Answer id="A3">
                            <Text> Kap Tormentoso </Text>
                            <ItemRef xlink:href="Punkt_B_E1" />
                            <FeatureRef xlink:href="#Punkt_B_Feature" />
                        </Answer>
                        <Answer id="A4" >
                            <Text> OneTableClub </Text>
                            <ItemRef xlink:href="Punkt_B_E2" />
                        </Answer>
                    </WayChooser>
                    <Item id="Punkt_B_E1">
                        <Description> Entscheidung 1 bei Punkt B </Description>
                        <FeatureRef xlink:href="#Punkt_B_Feature" />
                        <IsCollected>true</IsCollected>
                    </Item>
                    <Item id="Punkt_B_E2">
                        <Description> Entscheidung 2 bei Punkt B </Description>
                        <FeatureRef xlink:href="#Punkt_B_Feature" />
                        <IsCollected>true</IsCollected>
                    </Item>
                    <!-- ... Nach 28.30 Min keinen Bock mehr -->
                    <Item id="Punkt_C_1_Item">
                        <Description> Priatenzeug </Description>
                        <FeatureRef xlink:href="#Punkt_C_1_Feature" />
                        <IsCollected>true</IsCollected>
                    </Item>
                    <Item id="Punkt_C_2_Item">
                        <Description> gebrauchter Rucksack </Description>
                        <FeatureRef xlink:href="#Punkt_C_2_Feature" />
                        <IsCollected>true</IsCollected>
                    </Item>
                </Interactions>	
            </ARElements>
            <!-- Dauer: 28.30 + 15 + 13 Min +15 Min= 71.30 Min -->
        </arml>" 
        """


    ]

