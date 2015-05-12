var mainApp;

mainApp = angular.module("mainApp", ['ui.codemirror']);

mainApp.controller("mainCtrl", [
  "$scope", "$http", function($scope, $http) {
    var AddoDeleteNewNodes, addMarker, btnEinklappen, btnSwitchDown, btnSwitchUp, createChooser, createDropdownQuiz, createItem, createQuiz, createQuizDropdown, getAllStorypoints, googleMap, inIDSetter, initDdnInteraction, initDropdownClicks, initHelpSystem, initScrollbar, lightBox, lightMedienBox, setAllMap, setIDs, setQuizDropdownIDs;
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
      $("#btnMap").click(function() {
        return window.mapCaller = "btnMap";
      });
      lightMedienBox();
      initDropdownClicks();
      googleMap();
      window.dropdownLiCounter = 0;
      window.nodes = [];
      window.edges = [];
      initHelpSystem();
      initScrollbar();
    });
    initScrollbar = function() {
      var $header, $win, headerPos, scrollBottom;
      $header = $('#colColumn2');
      headerPos = $header.position().top;
      $win = $(window);
      scrollBottom = $win.scrollTop() + $win.height();
      return $win.scroll(function() {
        var i;
        if ($win.scrollTop() >= headerPos) {
          $header.css({
            'position': 'fixed',
            'top': '30px',
            'right': '20px'
          });
        }
        i = 0;
        while (i <= headerPos) {
          if ($win.scrollTop() === i) {
            $header.css({
              'position': 'fixed',
              'top': Math.abs(i - 150),
              'right': '20px'
            });
          }
          i++;
        }
      });
    };
    initDropdownClicks = function() {
      $("#ddnme").click(function() {
        if ($("#inRadius").val() !== "" && $("#ddnradius").val() === "Kilometer") {
          $("#inRadius").val(Math.round($("#inRadius").val() * 1000));
        }
        $("#ddnradius").val("Meter");
        return $("#ddnradius").html("Meter <span class='caret' />");
      });
      $("#ddnkm").click(function() {
        if ($("#inRadius").val() !== "" && $("#ddnradius").val() === "Meter") {
          $("#inRadius").val(Math.round($("#inRadius").val() / 1000));
        }
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
    $scope.createNewStorypoint = function(counter) {
      var button, copyForm, stuff;
      copyForm = document.getElementById("fhlNeuerStorypoint");
      stuff = copyForm.cloneNode(true);
      if (counter === void 0) {
        counter = 1;
      }
      stuff.id = "fhlNeuerStorypoint_" + counter;
      stuff.style.display = "block";
      document.getElementById("fhlStorypoints").appendChild(stuff);
      setIDs($("#" + stuff.id), counter);
      $("#lblInputStorypoint_" + counter).attr("for", "inStorypoint_" + counter);
      $("#inStorypoint_" + counter).keyup(function() {
        return inIDSetter($("#inStorypoint_" + counter), $("#lgdNeuerStorypointFieldset_" + counter), "Storypoint: ", "Neuer Storypoint");
      });
      $("#" + stuff.id).find("#btnSwitchDown").attr("id", "btnSwitchDown_" + counter);
      $("#" + stuff.id).find("#btnSwitchUp").attr("id", "btnSwitchUp_" + counter);
      btnSwitchDown("#btnSwitchDown_" + counter, "#" + stuff.id);
      btnSwitchUp("#btnSwitchUp_" + counter, "#" + stuff.id);
      $("#btnStorypointMap_" + counter).attr("gpsField", $("#btnStorypointMap_" + counter).attr("gpsField") + "_" + counter);
      $("#btnStorypointMap_" + counter).click(function() {
        return window.mapCaller = "btnStorypointMap_" + counter;
      });
      $("#btnNeuesStorypointDelete_" + counter).click(function() {
        if (confirm("Wollen Sie den Storypoint wirklich löschen?")) {
          AddoDeleteNewNodes("", $("#fhlNeuerStorypoint_" + counter).attr("nodeOwner"), counter);
          return $("#fhlNeuerStorypoint_" + counter).toggle("explode", {
            pieces: 25
          }, 2000, function() {
            return $("#fhlNeuerStorypoint_" + counter).remove();
          });
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
      button.scrollIntoView(true);
      $("#fhlNeuerStorypoint_" + counter).attr("nodeOwner", "Storypoint_" + counter);
      return AddoDeleteNewNodes("Storypoint: " + counter, "", counter);
    };
    inIDSetter = function(objectInput, objectFieldset, text, alternativeText) {
      objectInput.val(objectInput.val().replace(/\s+/, ""));
      if (objectInput.val() !== "") {
        text = text + objectInput.val();
        return objectFieldset.text(text);
      } else {
        return objectFieldset.text(alternativeText);
      }
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
    setIDs = function(node, counter) {
      node.children().each(function() {
        var newFor, newID;
        if (typeof $(this).attr("id") !== "undefined") {
          newID = $(this).attr("id") + "_" + counter;
          $(this).attr("id", newID);
          if (typeof $(this).attr("for") !== "undefined") {
            newFor = $(this).attr("for") + "_" + counter;
            $(this).attr("for", newFor);
          }
        }
        return setIDs($(this), counter);
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
      setIDs($("#" + stuff.id), interactionCounter);
      btnSwitchDown("#btnSwitchDown_" + interactionCounter, "#" + stuff.id);
      btnSwitchUp("#btnSwitchUp_" + interactionCounter, "#" + stuff.id);
      $("#btnItemDelete_" + interactionCounter).click(function() {
        if (confirm('Möchten Sie das Item wirklich löschen?')) {
          return $("#fgpNeu_Item_" + interactionCounter).remove();
        }
      });
      btnEinklappen("#btnItemEinklappen_" + interactionCounter, "#fstNeuesItemContent_" + interactionCounter);
      $("#inItemID_" + interactionCounter).keyup(function() {
        return inIDSetter($("#inItemID_" + interactionCounter), $("#lgdNeuesItemFieldset_" + interactionCounter), "Item: ", "Neues Item");
      });
      $("#inItemStorypointRef_" + interactionCounter).val($("#inStorypoint_" + counter).val());
      return $("#inStorypoint_" + counter).on('input', function() {
        var objectInput;
        objectInput = $("#inItemStorypointRef_" + interactionCounter);
        objectInput.val($("#inStorypoint_" + counter).val());
        if (typeof objectInput.val() !== "undefined") {
          return objectInput.val(objectInput.val().replace(/\s+/, ""));
        }
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
      setIDs($("#" + stuff.id), interactionCounter);
      btnSwitchDown("#btnSwitchDown_" + interactionCounter, "#" + stuff.id);
      btnSwitchUp("#btnSwitchUp_" + interactionCounter, "#" + stuff.id);
      $("#btnQuizDelete_" + interactionCounter).click(function() {
        if (confirm('Möchten Sie das Quiz wirklich löschen?')) {
          return $("#fgpNeu_Quiz_" + interactionCounter).remove();
        }
      });
      $("#btnSetQuizOnTrueReferences_" + interactionCounter).click(function() {
        return createDropdownQuiz(interactionCounter, "#btnSetQuizOnTrueReferences_" + interactionCounter, "#" + stuff.id);
      });
      btnEinklappen("#btnQuizEinklappen_" + interactionCounter, "#fstNeuesQuizContent_" + interactionCounter);
      $("#inQuizID_" + interactionCounter).keyup(function() {
        return inIDSetter($("#inQuizID_" + interactionCounter), $("#lgdNeuesQuizFieldset_" + interactionCounter), "Quiz: ", "Neues Quiz");
      });
      $("#inQuizStorypointRef_" + interactionCounter).val($("#inStorypoint_" + counter).val());
      $("#inStorypoint_" + counter).on('input', function() {
        var objectInput;
        objectInput = $("#inQuizStorypointRef_" + interactionCounter);
        objectInput.val($("#inStorypoint_" + counter).val());
        if (typeof objectInput.val() !== "undefined") {
          return objectInput.val(objectInput.val().replace(/\s+/, ""));
        }
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
        setIDs($("#" + answer.id), quizAnswerCounter);
        createQuizDropdown(quizAnswerCounter);
        return $("#btnQuizAnswerDelete_" + quizAnswerCounter).click(function() {
          if (confirm('Möchten Sie die Antwort wirklich löschen?')) {
            return $("#" + answer.id).remove();
          }
        });
      });
    };
    createQuizDropdown = function(counter) {
      $("#ddnTrue_" + counter).click(function() {
        $("#ddnState_" + counter).val("Wahr");
        return $("#ddnState_" + counter).html("Wahr <span class='caret' />");
      });
      return $("#ddnFalse_" + counter).click(function() {
        $("#ddnState_" + counter).val("Falsch");
        return $("#ddnState_" + counter).html("Falsch <span class='caret' />");
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
      setIDs($("#" + stuff.id), interactionCounter);
      btnSwitchDown("#btnSwitchDown_" + interactionCounter, "#" + stuff.id);
      btnSwitchUp("#btnSwitchUp_" + interactionCounter, "#" + stuff.id);
      $("#btnChooserDelete_" + interactionCounter).click(function() {
        if (confirm('Möchten Sie den Chooser wirklich löschen?')) {
          return $("#fgpNeu_Chooser_" + interactionCounter).remove();
        }
      });
      btnEinklappen("#btnChooserEinklappen_" + interactionCounter, "#fstNeuerChooserContent_" + interactionCounter);
      $("#inChooserID_" + interactionCounter).keyup(function() {
        return inIDSetter($("#inChooserID_" + interactionCounter), $("#lgdNeuerChooserFieldset_" + interactionCounter), "Chooser: ", "Neuer Chooser");
      });
      $("#inChooserStorypointRef_" + interactionCounter).val($("#inStorypoint_" + counter).val());
      $("#inStorypoint_" + counter).on('input', function() {
        var objectInput;
        objectInput = $("#inChooserStorypointRef_" + interactionCounter);
        objectInput.val($("#inStorypoint_" + counter).val());
        if (typeof objectInput.val() !== "undefined") {
          return objectInput.val(objectInput.val().replace(/\s+/, ""));
        }
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
        setIDs($("#" + answer.id), ChooserAnswerCounter);
        return $("#btnChooserAnswerDelete_" + ChooserAnswerCounter).click(function() {
          if (confirm('Möchten Sie die Antwort wirklich löschen?')) {
            return $("#" + answer.id).remove();
          }
        });
      });
    };
    btnSwitchDown = function(buttonID, currentObjID) {
      $(buttonID).click(function() {
        var currentObject, nextObject;
        nextObject = $(currentObjID).next();
        currentObject = $(currentObjID);
        if (typeof nextObject.attr("id") === 'undefined' || nextObject.prop('tagName') !== currentObject.prop('tagName')) {
          currentObject.effect("shake");
          return;
        } else {
          currentObject.animate({
            opacity: 0.25
          }, 400, function() {
            return nextObject.insertBefore("#" + currentObject.attr("id"));
          });
          currentObject.animate({
            opacity: 1
          }, 500);
        }
      });
    };
    btnSwitchUp = function(buttonID, currentObjID) {
      $(buttonID).click(function() {
        var currentObject, prevObject;
        prevObject = $(currentObjID).prev();
        currentObject = $(currentObjID);
        if (typeof prevObject.attr("id") === 'undefined' || prevObject.prop('tagName') !== currentObject.prop('tagName')) {
          currentObject.effect("shake");
          return;
        } else {
          currentObject.animate({
            opacity: 0.25
          }, 400, function() {
            return prevObject.insertAfter("#" + currentObject.attr("id"));
          });
          currentObject.animate({
            opacity: 1
          }, 500);
        }
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
    getAllStorypoints = function(buttonID, currentStorypointID) {
      var currentStorypoint, nextStorypoint, prevStorypoint, storypointArray;
      prevStorypoint = $(currentStorypointID).prev();
      nextStorypoint = $(currentStorypointID).next();
      currentStorypoint = $(currentStorypointID);
      storypointArray = [];
      while (typeof prevStorypoint.attr("id") !== 'undefined') {
        storypointArray.push(prevStorypoint);
        prevStorypoint = prevStorypoint.prev();
      }
      storypointArray.push(currentStorypoint);
      while (typeof nextStorypoint.attr("id") !== 'undefined' && nextStorypoint.attr("id") !== "fgpStorypoint") {
        storypointArray.push(nextStorypoint);
        nextStorypoint = nextStorypoint.next();
      }
      return storypointArray;
    };
    setQuizDropdownIDs = function(node, lauf) {
      node.children().each(function() {
        var newID;
        if (typeof $(this).attr("id") !== "undefined") {
          newID = $(this).attr("id") + "_" + window.dropdownLiCounter + "_" + lauf;
          $(this).attr("id", newID);
          lauf++;
        }
        return setQuizDropdownIDs($(this), lauf);
      });
    };
    createDropdownQuiz = function(counter, buttonID, currentObjID) {
      var copyForm, currentStorypointID, i, indexe, j, lauf, storypointArray, stuff, tmpStoryname;
      currentStorypointID = $(currentObjID).closest(".form-horizontal").attr("id");
      storypointArray = getAllStorypoints(buttonID, "#" + currentStorypointID);
      copyForm = document.getElementById("liSkQuizOnTrueRef");
      window.dropdownLiCounter++;
      $("#ulSkQuizOnTrueRef_" + counter).empty();
      i = 0;
      while (i < storypointArray.length) {
        stuff = copyForm.cloneNode(true);
        stuff.id = stuff.id + "_" + window.dropdownLiCounter;
        stuff.style.display = "block";
        document.getElementById("ulSkQuizOnTrueRef_" + counter).appendChild(stuff);
        i++;
      }
      lauf = 0;
      setQuizDropdownIDs($("#ulSkQuizOnTrueRef_" + counter), lauf);
      j = 0;
      while (j < storypointArray.length) {
        indexe = window.dropdownLiCounter + "_" + (j + 1);
        tmpStoryname = storypointArray[j].children("fieldset").children("fieldset").find("#inStorypoint_" + (j + 1)).val();
        if (tmpStoryname === "") {
          tmpStoryname = storypointArray[j].children("fieldset").children("fieldset").find("#inStorypoint_" + (j + 1)).attr("placeholder") + (j + 1);
        }
        $("#ddnQuizOnTrueStorypoint_" + indexe).click(function() {
          $("#btnSetQuizOnTrueReferences_" + counter).val("" + tmpStoryname);
          $("#btnSetQuizOnTrueReferences_" + counter).html(tmpStoryname + "<span class='caret' />");
        });
        j++;
      }
    };
    AddoDeleteNewNodes = function(nodeLabelInfo, searchID, counter) {
      var container, data, i, network, node;
      if (searchID !== '') {
        i = 0;
        while (i < window.nodes.length) {
          if (window.nodes[i].nodeOwner === searchID) {
            window.nodes.splice(i, 1);
          }
          i++;
        }
      } else {
        node = {
          id: counter,
          label: nodeLabelInfo
        };
        node.nodeOwner = "Storypoint_" + counter;
        window.nodes.push(node);
      }
      container = document.getElementById('divDependencyBox');
      data = {
        nodes: window.nodes,
        edges: window.edges
      };
      network = new vis.Network(container, data, {});
    };
    googleMap = function() {
      var autocomplete, input, mapOptions, markers, searchBox;
      mapOptions = {
        zoom: 8,
        center: new google.maps.LatLng(48.7758459, 9.182932100000016),
        scaleControl: true
      };
      window.map = new google.maps.Map($('#gmeg_map_canvas')[0], mapOptions);
      input = document.getElementById('inMapSearch');
      window.map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
      searchBox = new google.maps.places.SearchBox(input);
      autocomplete = new google.maps.places.Autocomplete(input, {
        types: ['geocode']
      });
      autocomplete.bindTo('bounds', window.map);
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
            map: window.map,
            icon: image,
            title: place.name,
            position: place.geometry.location
          });
          markers.push(marker);
          $("#inLatLngLocation").val(place.geometry.location.A + ", " + place.geometry.location.F);
          bounds.extend(place.geometry.location);
          i++;
        }
        window.map.fitBounds(bounds);
      });
      $(window).resize(function() {
        google.maps.event.trigger(window.map, 'resize');
      });
      google.maps.event.addListener(window.map, 'click', function(event) {
        var currentGPSField, deleted, i, lat, lng, max;
        lat = event.latLng.lat();
        lng = event.latLng.lng();
        currentGPSField = $("#" + window.mapCaller).attr("gpsField");
        $("#" + currentGPSField).val(lat + ", " + lng);
        i = 0;
        max = markers.length;
        deleted = [];
        while (i < max) {
          markers[i].setMap(null);
          if (markers[i].get("markerOwner") === window.mapCaller) {
            deleted.push(i);
          }
          i++;
        }
        i = deleted.length;
        i--;
        while (i > -1) {
          markers.splice(deleted[i], 1);
          i--;
        }
        addMarker(event.latLng, markers);
      });
      return lightBox(markers);
    };
    lightBox = function(markers) {
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
        google.maps.event.trigger(window.map, 'resize');
      });
      $lightbox.on('shown.bs.modal', function(e) {
        var $mapDiv, i;
        $mapDiv = $(this).find('gmeg_map_canvas');
        $lightbox.find('.modal-dialog').css({
          'width': $mapDiv.width()
        });
        $lightbox.find('.close').removeClass('hidden');
        i = 0;
        while (i < markers.length) {
          if (markers[i].get("markerOwner") === window.mapCaller) {
            markers[i].setMap(window.map);
            markers[i].getMap().setCenter(markers[i].position);
          }
          i++;
        }
        google.maps.event.trigger(window.map, 'resize');
      });
      return $lightbox.on('hide.bs.modal', function(e) {
        return setAllMap(null, markers);
      });
    };
    lightMedienBox = function() {
      var $medien;
      $medien = $('#medienLightbox');
      $('[data-target="#medienLightbox"]').on('click', function(event) {
        var $medienBib, css;
        $medienBib = $(this).find('medienBib');
        css = {
          'maxWidth': $(window).width() - 100,
          'maxHeight': $(window).height() - 100
        };
        $medien.find('.close').addClass('hidden');
        $medienBib.css(css);
      });
      return $medien.on('shown.bs.modal', function(e) {
        var $medienBib;
        $medienBib = $(this).find('medienBib');
        $medien.find('.modal-dialog').css({
          'width': $medienBib.width()
        });
        $medien.find('.close').removeClass('hidden');
      });
    };
    addMarker = function(location, markers) {
      var center, circle, marker, rad;
      center = new google.maps.LatLng(location.A, location.F);
      marker = new google.maps.Marker({
        position: location,
        map: window.map
      });
      marker.set("markerOwner", window.mapCaller);
      markers.push(marker);
      if (window.mapCaller === "btnMap" && $("#ddnradius").val() !== "Einheit") {
        rad = $("#inRadius").val();
        if ($("#ddnradius").val() === "Einheit") {
          rad = 0;
        } else if ($("#ddnradius").val() === "Kilometer") {
          rad = rad * 1000;
        }
        circle = new google.maps.Circle({
          center: center,
          map: window.map,
          radius: Math.round(rad),
          strokeColor: 'red',
          strokeOpacity: 0.8,
          strokeWeight: 2,
          fillColor: 'blue',
          fillOpacity: 0.35,
          editable: true
        });
        circle.set("markerOwner", window.mapCaller);
        markers.push(circle);
        google.maps.event.addListener(circle, 'radius_changed', function() {
          rad = circle.getRadius();
          if ($("#ddnradius").val() === "Meter") {
            return $("#inRadius").val(Math.round(rad));
          } else if ($("#ddnradius").val() === "Kilometer") {
            return $("#inRadius").val(Math.round(rad / 1000));
          }
        });
        google.maps.event.addListener(circle, 'center_changed', function() {
          return marker.setPosition(circle.center);
        });
        google.maps.event.addListener(marker, 'position_changed', function() {
          var lat, lng;
          lat = marker.position.A;
          lng = marker.position.F;
          return $("#inLatLngLocation").val(lat + ", " + lng);
        });
        return;
      }
    };
    initHelpSystem = function() {
      $.ajax({
        type: 'GET',
        url: 'HelpContent.xml',
        dataType: 'xml',
        success: function(xml) {
          $("#divHelpBox").append($(xml).find('inTitle').text());
        }
      });
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
