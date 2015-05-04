var mainApp;

mainApp = angular.module("mainApp", ['ui.codemirror']);

mainApp.controller("mainCtrl", [
  "$scope", "$http", function($scope, $http) {
    var addMarker, btnEinklappen, createChooser, createItem, createQuiz, googleMap, graph, initDdnInteraction, initDropdownClicks, lightBox, setAllMap;
    $scope.editorOptions = {
      lineNumbers: true,
      mode: 'xml',
      theme: "eclipse"
    };
    $scope.storySelected = false;
    $scope.handleStorySelected = function(story) {
      $scope.storySelected = true;
      return $http.get("http://api.dev.la/story/" + story.id).success(function(data) {
        return $scope.xmlFile = data;
      });
    };
    $http.get("http://api.dev.la/stories").success(function(data) {
      return $scope.storys = data;
    });
    $scope.createStory = function() {
      return window.location.href = '../createStory.html';
    };
    $scope.deleteStory = function() {
      return console.log("Gelöscht");
    };
    $scope.getFormulars = function() {
      document.getElementById("createStoryRow").style.display = "none";
      return document.getElementById("Formular").style.display = "block";
    };
    (function($) {})(jQuery);
    $(function() {
      initDropdownClicks();
      googleMap();
      graph();
    });
    initDropdownClicks = function() {
      $("#ddnme").click(function() {
        $("#ddnradius").val("Meter");
        return $("#ddnradius").html("Meter <span class='caret' />");
      });
      $("#ddnkm").click(function() {
        $("#ddnradius").val("Kilometer");
        return $("#ddnradius").html("Kilometer <span class='caret' />");
      });
      $("#ddnkb").click(function() {
        $("#ddnsize").val("KB");
        return $("#ddnsize").html("KB <span class='caret' />");
      });
      $("#ddnmb").click(function() {
        $("#ddnsize").val("MB");
        return $("#ddnsize").html("MB <span class='caret' />");
      });
      return $("#ddngb").click(function() {
        $("#ddnsize").val("KB");
        return $("#ddnsize").html("GB <span class='caret' />");
      });
    };
    $scope.createNewFeature = function(counter) {
      var button, copyForm, stuff;
      copyForm = document.getElementById("fhlNeuerStorypoint");
      stuff = copyForm.cloneNode(true);
      if (counter === void 0) {
        counter = 1;
      }
      stuff.id = "fhlNeuerStorypoint_" + counter;
      stuff.style.display = "block";
      document.getElementById("fhlStorypoints").appendChild(stuff);
      $("#" + stuff.id).find("#lgdNeuerStorypointFieldset").attr("id", "lgdNeuerStorypointFieldset_" + counter);
      $("#" + stuff.id).find("#btnNeuesStorypointDelete").attr("id", "btnNeuesStorypointDelete_" + counter);
      $("#" + stuff.id).find("#btnStorypointEinklappen").attr("id", "btnStorypointEinklappen_" + counter);
      $("#" + stuff.id).find("#fstNeuesStorypointContent").attr("id", "fstNeuesStorypointContent_" + counter);
      $("#" + stuff.id).find("#lblInputStorypoint").attr("id", "lblInputStorypoint_" + counter);
      $("#" + stuff.id).find("#inStorypoint").attr("id", "inStorypoint_" + counter);
      $("#" + stuff.id).find("#bgpControlGroup").attr("id", "bgpControlGroup_" + counter);
      $("#" + stuff.id).find("#btnCreateInteraction").attr("id", "btnCreateInteraction_" + counter);
      $("#" + stuff.id).find("#ddnInteractions").attr("id", "ddnInteractions_" + counter);
      $("#" + stuff.id).find("#ddnChooser").attr("id", "ddnChooser_" + counter);
      $("#" + stuff.id).find("#ddnQuiz").attr("id", "ddnQuiz_" + counter);
      $("#" + stuff.id).find("#ddnItem").attr("id", "ddnItem_" + counter);
      $("#lblInputStorypoint_" + counter).attr("for", "inStorypoint_" + counter);
      $("#inStorypoint_" + counter).keyup(function() {
        var text;
        text = "Storypoint: " + $("#inStorypoint_" + counter).val();
        return $("#lgdNeuerStorypointFieldset_" + counter).text(text);
      });
      $("#btnNeuesStorypointDelete_" + counter).click(function() {
        if (confirm('Möchten Sie den Storypoint wirklich löschen?')) {
          return $("#fhlNeuerStorypoint_" + counter).remove();
        }
      });
      btnEinklappen("#btnStorypointEinklappen_" + counter, "#fstNeuesStorypointContent_" + counter);
      $("#btnCreateInteraction_" + counter).attr("interactionCounter", counter);
      $("#btnCreateInteraction_" + counter).click(function() {
        if ($("#ddnInteractions_" + counter).val() === "Item") {
          return createItem(counter);
        } else if ($("#ddnInteractions_" + counter).val() === "Quiz") {
          return createQuiz(counter);
        } else if ($("#ddnInteractions_" + counter).val() === "Chooser") {
          return createChooser(counter);
        }
      });
      initDdnInteraction(counter);
      button = document.getElementById("fgpStorypoint");
      button.parentNode.removeChild(button);
      document.getElementById("fhlStorypoints").appendChild(button);
      return button.scrollIntoView(true);
    };
    initDdnInteraction = function(counter) {
      $("#ddnChooser_" + counter).click(function() {
        $("#ddnInteractions_" + counter).val("Chooser");
        return $("#ddnInteractions_" + counter).html("Chooser <span class='caret'/>");
      });
      $("#ddnQuiz_" + counter).click(function() {
        $("#ddnInteractions_" + counter).val("Quiz");
        return $("#ddnInteractions_" + counter).html("Quiz <span class='caret'/>");
      });
      return $("#ddnItem_" + counter).click(function() {
        $("#ddnInteractions_" + counter).val("Item");
        return $("#ddnInteractions_" + counter).html("Item <span class='caret'/>");
      });
    };
    createItem = function(counter) {
      var copyForm, interactionCounter, stuff;
      copyForm = document.getElementById("fgpNeu_" + $("#ddnInteractions_" + counter).val());
      interactionCounter = $("#btnCreateInteraction_" + counter).attr("interactionCounter");
      interactionCounter++;
      $("#btnCreateInteraction_" + counter).attr("interactionCounter", interactionCounter);
      stuff = copyForm.cloneNode(true);
      stuff.id = stuff.id + "_" + interactionCounter;
      stuff.style.display = "block";
      document.getElementById("fstNeuesStorypointContent_" + counter).appendChild(stuff);
      $("#" + stuff.id).find("#lgdNeuesItemFieldset").attr("id", "lgdNeuesItemFieldset_" + interactionCounter);
      $("#" + stuff.id).find("#fstNeuesItemContent").attr("id", "fstNeuesItemContent_" + interactionCounter);
      $("#" + stuff.id).find("#btnItemControlGroup").attr("id", "btnItemControlGroup_" + interactionCounter);
      $("#" + stuff.id).find("#btnItemEinklappen").attr("id", "btnItemEinklappen_" + interactionCounter);
      $("#" + stuff.id).find("#btnItemDelete").attr("id", "btnItemDelete_" + interactionCounter);
      $("#" + stuff.id).find("#inItemID").attr("id", "inItemID_" + interactionCounter);
      $("#" + stuff.id).find("#inItemDescription").attr("id", "itemDescription_" + interactionCounter);
      $("#" + stuff.id).find("#itemFeatureRef").attr("id", "itemFeatureRef_" + interactionCounter);
      $("#" + stuff.id).find("#inItemIsCollected").attr("id", "inItemIsCollected_" + interactionCounter);
      $("#" + stuff.id).find("#lblinItemID").attr("id", "lblinItemID_" + interactionCounter);
      $("#" + stuff.id).find("#lblItemIsCollected").attr("id", "lblItemIsCollected_" + interactionCounter);
      $("#" + stuff.id).find("#lblItemDescription").attr("id", "lblItemDescription_" + interactionCounter);
      $("#" + stuff.id).find("#lblItemFeatureRef").attr("id", "lblItemStorypointRef_" + interactionCounter);
      $("#lblinItemID_" + interactionCounter).attr("for", "inItemID_" + interactionCounter);
      $("#lblItemIsCollected_" + interactionCounter).attr("for", "inItemIsCollected_" + interactionCounter);
      $("#lblItemDescription_" + interactionCounter).attr("for", "inItemDescription_" + interactionCounter);
      $("#lblItemStorypointRef_" + interactionCounter).attr("for", "itemFeatureRef_" + interactionCounter);
      $("#itemFeatureRef_" + interactionCounter).val("NeuesFeature_" + counter);
      $("#btnItemDelete_" + interactionCounter).click(function() {
        if (confirm('Möchten Sie das Item wirklich löschen?')) {
          return $("#fgpNeu_Item_" + interactionCounter).remove();
        }
      });
      btnEinklappen("#btnItemEinklappen_" + interactionCounter, "#fstNeuesItemContent_" + interactionCounter);
      return $("#inItemID_" + interactionCounter).keyup(function() {
        var text;
        text = "Item: " + $("#inItemID_" + interactionCounter).val();
        return $("#lgdNeuesItemFieldset_" + interactionCounter).text(text);
      });
    };
    createQuiz = function(counter) {
      var copyForm, interactionCounter, stuff;
      copyForm = document.getElementById("fgpNeu_" + $("#ddnInteractions_" + counter).val());
      interactionCounter = $("#btnCreateInteraction_" + counter).attr("interactionCounter");
      interactionCounter++;
      $("#btnCreateInteraction_" + counter).attr("interactionCounter", interactionCounter);
      stuff = copyForm.cloneNode(true);
      stuff.id = stuff.id + "_" + interactionCounter;
      stuff.style.display = "block";
      document.getElementById("fstNeuesStorypointContent_" + counter).appendChild(stuff);
      $("#" + stuff.id).find("#lgdNeuesQuizFieldset").attr("id", "lgdNeuesQuizFieldset_" + interactionCounter);
      $("#" + stuff.id).find("#fstNeuesQuizContent").attr("id", "fstNeuesQuizContent_" + interactionCounter);
      $("#" + stuff.id).find("#btnQuizControlGroup").attr("id", "btnQuizControlGroup_" + interactionCounter);
      $("#" + stuff.id).find("#btnQuizEinklappen").attr("id", "btnQuizEinklappen_" + interactionCounter);
      $("#" + stuff.id).find("#btnQuizDelete").attr("id", "btnQuizDelete_" + interactionCounter);
      $("#" + stuff.id).find("#inQuizID").attr("id", "inQuizID_" + interactionCounter);
      $("#" + stuff.id).find("#inQuizStorypointRef").attr("id", "inQuizStorypointRef_" + interactionCounter);
      $("#" + stuff.id).find("#inQuizOnTrue").attr("id", "inQuizOnTrue_" + interactionCounter);
      $("#" + stuff.id).find("#inQuizOnFalse").attr("id", "inQuizOnFalse_" + interactionCounter);
      $("#" + stuff.id).find("#inQuizQuestion").attr("id", "inQuizQuestion_" + interactionCounter);
      $("#" + stuff.id).find("#btnQuizAnswer").attr("id", "btnQuizAnswer_" + interactionCounter);
      $("#btnQuizDelete_" + interactionCounter).click(function() {
        if (confirm('Möchten Sie das Quiz wirklich löschen?')) {
          return $("#fgpNeu_Quiz_" + interactionCounter).remove();
        }
      });
      btnEinklappen("#btnQuizEinklappen_" + interactionCounter, "#fstNeuesQuizContent_" + interactionCounter);
      $("#inQuizID_" + interactionCounter).keyup(function() {
        var text;
        text = "Quiz: " + $("#inQuizID_" + interactionCounter).val();
        return $("#lgdNeuesQuizFieldset_" + interactionCounter).text(text);
      });
      return $("#btnQuizAnswer_" + interactionCounter).click(function() {
        var answer, copyAnswer, quizAnswerCounter;
        quizAnswerCounter = $("#btnQuizAnswer_" + interactionCounter).attr("quizAnswerCounter");
        quizAnswerCounter++;
        $("#btnQuizAnswer_" + interactionCounter).attr("quizAnswerCounter", quizAnswerCounter);
        copyAnswer = document.getElementById("fgpAnswer");
        answer = copyAnswer.cloneNode(true);
        answer.id = answer.id + "_" + quizAnswerCounter;
        answer.style.display = "block";
        document.getElementById("fstNeuesQuizContent_" + interactionCounter).appendChild(answer);
        $("#" + answer.id).find("#lblQuizAnswerID").attr("id", "lblQuizAnswerID_" + quizAnswerCounter);
        $("#" + answer.id).find("#inQuizAnswerID").attr("id", "inQuizAnswerID_" + quizAnswerCounter);
        $("#" + answer.id).find("#lblQuizAnswerText").attr("id", "lblQuizAnswerText_" + quizAnswerCounter);
        $("#" + answer.id).find("#inQuizAnswerText").attr("id", "inQuizAnswerText_" + quizAnswerCounter);
        $("#" + answer.id).find("#btnQuizAnswerDelete").attr("id", "btnQuizAnswerDelete_" + quizAnswerCounter);
        return $("#btnQuizAnswerDelete_" + quizAnswerCounter).click(function() {
          if (confirm('Möchten Sie die Antwort wirklich löschen?')) {
            return $("#" + answer.id).remove();
          }
        });
      });
    };
    createChooser = function(counter) {
      var copyForm, interactionCounter, stuff;
      copyForm = document.getElementById("fgpNeu_" + $("#ddnInteractions_" + counter).val());
      interactionCounter = $("#btnCreateInteraction_" + counter).attr("interactionCounter");
      interactionCounter++;
      $("#btnCreateInteraction_" + counter).attr("interactionCounter", interactionCounter);
      stuff = copyForm.cloneNode(true);
      stuff.id = stuff.id + "_" + interactionCounter;
      stuff.style.display = "block";
      document.getElementById("fstNeuesStorypointContent_" + counter).appendChild(stuff);
      $("#" + stuff.id).find("#lgdNeuerChooserFieldset").attr("id", "lgdNeuerChooserFieldset_" + interactionCounter);
      $("#" + stuff.id).find("#fstNeuerChooserContent").attr("id", "fstNeuerChooserContent_" + interactionCounter);
      $("#" + stuff.id).find("#bgpChooserControlGroup").attr("id", "bgpChooserControlGroup_" + interactionCounter);
      $("#" + stuff.id).find("#btnChooserEinklappen").attr("id", "btnChooserEinklappen_" + interactionCounter);
      $("#" + stuff.id).find("#btnChooserDelete").attr("id", "btnChooserDelete_" + interactionCounter);
      $("#" + stuff.id).find("#inChooserID").attr("id", "inChooserID_" + interactionCounter);
      $("#" + stuff.id).find("#inChooserStorypointRef").attr("id", "inChooserStorypointRef_" + interactionCounter);
      $("#" + stuff.id).find("#inChooserQuestion").attr("id", "inChooserQuestion_" + interactionCounter);
      $("#" + stuff.id).find("#btnChooserAnswer").attr("id", "btnChooserAnswer_" + interactionCounter);
      $("#btnChooserDelete_" + interactionCounter).click(function() {
        if (confirm('Möchten Sie den Chooser wirklich löschen?')) {
          return $("#fgpNeu_Chooser_" + interactionCounter).remove();
        }
      });
      btnEinklappen("#btnChooserEinklappen_" + interactionCounter, "#fstNeuerChooserContent_" + interactionCounter);
      $("#inChooserID_" + interactionCounter).keyup(function() {
        var text;
        text = "Chooser: " + $("#inChooserID_" + interactionCounter).val();
        return $("#lgdNeuerChooserFieldset_" + interactionCounter).text(text);
      });
      return $("#btnChooserAnswer_" + interactionCounter).click(function() {
        var ChooserAnswerCounter, answer, copyAnswer;
        ChooserAnswerCounter = $("#btnChooserAnswer_" + interactionCounter).attr("ChooserAnswerCounter");
        ChooserAnswerCounter++;
        $("#btnChooserAnswer_" + interactionCounter).attr("ChooserAnswerCounter", ChooserAnswerCounter);
        copyAnswer = document.getElementById("fgpChooserAnswer");
        answer = copyAnswer.cloneNode(true);
        answer.id = answer.id + "_" + ChooserAnswerCounter;
        answer.style.display = "block";
        document.getElementById("fstNeuerChooserContent_" + interactionCounter).appendChild(answer);
        $("#" + answer.id).find("#lblChooserAnswerID").attr("id", "lblChooserAnswerID_" + ChooserAnswerCounter);
        $("#" + answer.id).find("#inChooserAnswerID").attr("id", "inChooserAnswerID_" + ChooserAnswerCounter);
        $("#" + answer.id).find("#lblChooserAnswerText").attr("id", "lblChooserAnswerText_" + ChooserAnswerCounter);
        $("#" + answer.id).find("#inChooserAnswerText").attr("id", "inChooserAnswerText_" + ChooserAnswerCounter);
        $("#" + answer.id).find("#lblChooserAnswerItemRef").attr("id", "lblChooserAnswerItemRef_" + ChooserAnswerCounter);
        $("#" + answer.id).find("#inChooserAnswerItemRef").attr("id", "inChooserAnswerItemRef_" + ChooserAnswerCounter);
        $("#" + answer.id).find("#lblChooserAnswerFeatureRef").attr("id", "lblChooserAnswerFeatureRef_" + ChooserAnswerCounter);
        $("#" + answer.id).find("#inChooserAnswerStorypointRef").attr("id", "inChooserAnswerStorypointRef_" + ChooserAnswerCounter);
        $("#" + answer.id).find("#btnChooserAnswerDelete").attr("id", "btnChooserAnswerDelete_" + ChooserAnswerCounter);
        return $("#btnChooserAnswerDelete_" + ChooserAnswerCounter).click(function() {
          if (confirm('Möchten Sie die Antwort wirklich löschen?')) {
            return $("#" + answer.id).remove();
          }
        });
      });
    };
    btnEinklappen = function(button, content) {
      return $(button).click(function() {
        if ($(content).is(":hidden")) {
          $(content).show("slow");
          $(button).find("#resize").addClass('glyphicon-resize-small');
          return $(button).find("#resize").removeClass('glyphicon-resize-full');
        } else {
          $(content).slideUp("slow");
          $(button).find("#resize").removeClass('glyphicon-resize-small');
          return $(button).find("#resize").addClass('glyphicon-resize-full');
        }
      });
    };
    $scope.tabSwitch = function(activeTabID) {
      if (activeTabID === "MedienBibTab") {
        $("#MedienBibTab").addClass("active");
        $("#GraEditorTab").removeClass("active");
        $("#XMLTab").removeClass("active");
        $("#MedienEditor").css("display", "block");
        $("#GraEditor").css("display", "none");
        $("#XML").css("display", "none");
      } else if (activeTabID === "GraEditorTab") {
        $("#GraEditorTab").addClass("active");
        $("#MedienBibTab").removeClass("active");
        $("#XMLTab").removeClass("active");
        $("#GraEditor").css("display", "block");
        $("#XML").css("display", "none");
        $("#MedienEditor").css("display", "none");
      } else if (activeTabID === "XMLTab") {
        $("#XMLTab").addClass("active");
        $("#GraEditorTab").removeClass("active");
        $("#MedienBibTab").removeClass("active");
        $("#XML").css("display", "block");
        $("#MedienEditor").css("display", "none");
        $("#GraEditor").css("display", "none");
      }
    };
    graph = function() {
      var container, data, edges, network, nodes;
      nodes = [
        {
          id: 1,
          label: 'Node 1'
        }, {
          id: 2,
          label: 'Node 2'
        }, {
          id: 3,
          label: 'Node 3'
        }, {
          id: 4,
          label: 'Node 4'
        }, {
          id: 5,
          label: 'Node 5'
        }
      ];
      edges = [
        {
          from: 1,
          to: 2
        }, {
          from: 1,
          to: 3
        }, {
          from: 2,
          to: 4
        }, {
          from: 2,
          to: 5
        }
      ];
      container = document.getElementById('divDependencyBox');
      data = {
        nodes: nodes,
        edges: edges
      };
      network = new vis.Network(container, data, {});
    };
    googleMap = function() {
      var input, map, mapOptions, markers, searchBox;
      mapOptions = {
        zoom: 8,
        center: new google.maps.LatLng(48.7760745003604, 9.172875881195068)
      };
      map = new google.maps.Map($('#gmeg_map_canvas')[0], mapOptions);
      input = document.getElementById('inMapSearch');
      map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
      searchBox = new google.maps.places.SearchBox(input);
      markers = [];
      google.maps.event.addListener(searchBox, 'places_changed', function() {
        var marker;
        var i;
        var bounds, i, image, marker, place, places;
        places = searchBox.getPlaces();
        if (places.length === 0) {
          return;
        }
        i = 0;
        marker = void 0;
        if (typeof markers !== 'undefined') {
          while (marker = markers[i]) {
            marker.setMap(null);
            i++;
          }
        }
        markers = [];
        bounds = new google.maps.LatLngBounds;
        i = 0;
        place = void 0;
        while (place = places[i]) {
          image = {
            url: place.icon,
            size: new google.maps.Size(71, 71),
            origin: new google.maps.Point(0, 0),
            anchor: new google.maps.Point(17, 34),
            scaledSize: new google.maps.Size(25, 25)
          };
          setAllMap(null, markers);
          markers = [];
          marker = new google.maps.Marker({
            map: map,
            icon: image,
            title: place.name,
            position: place.geometry.location
          });
          markers.push(marker);
          bounds.extend(place.geometry.location);
          i++;
        }
        map.fitBounds(bounds);
      });
      $(window).resize(function() {
        google.maps.event.trigger(map, 'resize');
      });
      google.maps.event.addListener(map, 'click', function(event) {
        var i, lat, lng;
        lat = event.latLng.lat();
        lng = event.latLng.lng();
        $("#inMapSearch").val(lat + ", " + lng);
        $("#inLatLngLocation").val(lat + ", " + lng);
        i = 0;
        while (i < markers.length) {
          markers[i].setMap(null);
          i++;
        }
        addMarker(event.latLng, markers, map);
      });
      return lightBox(map);
    };
    lightBox = function(map) {
      var $lightbox;
      $lightbox = $('#lightbox');
      $('[data-target="#lightbox"]').on('click', function(event) {
        var $mapDiv, css;
        $mapDiv = $(this).find('gmeg_map_canvas');
        css = {
          'maxWidth': $(window).width() - 100,
          'maxHeight': $(window).height() - 100
        };
        $lightbox.find('.close').addClass('hidden');
        $mapDiv.css(css);
        google.maps.event.trigger(map, 'resize');
      });
      return $lightbox.on('shown.bs.modal', function(e) {
        var $mapDiv;
        $mapDiv = $(this).find('gmeg_map_canvas');
        $lightbox.find('.modal-dialog').css({
          'width': $mapDiv.width()
        });
        $lightbox.find('.close').removeClass('hidden');
        google.maps.event.trigger(map, 'resize');
      });
    };
    addMarker = function(location, markers, map) {
      var marker;
      marker = new google.maps.Marker({
        position: location,
        map: map
      });
      markers.push(marker);
    };
    setAllMap = function(map, markers) {
      var i;
      i = 0;
      while (i < markers.length) {
        markers[i].setMap(map);
        i++;
      }
    };
    return $scope.mediaData = [
      {
        id: 1,
        name: "Cover",
        type: "image"
      }, {
        id: 2,
        name: "ReferenceBar",
        type: "image"
      }, {
        id: 3,
        name: "Introduction",
        type: "movie"
      }, {
        id: 4,
        name: "FinalScene",
        type: "movie"
      }
    ];
  }
]);
