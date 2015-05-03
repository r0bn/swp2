var mainApp;

mainApp = angular.module("mainApp", ['ui.codemirror']);

mainApp.controller("mainCtrl", [
  "$scope", "$http", function($scope, $http) {
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
    (function($) {})(jQuery);
    $(function() {
      var $lightbox, map, mapOptions;
      mapOptions = {
        zoom: 8,
        center: new google.maps.LatLng(48.7760745003604, 9.172875881195068)
      };
      map = new google.maps.Map($('#gmeg_map_canvas')[0], mapOptions);
      $(window).resize(function() {
        google.maps.event.trigger(map, 'resize');
      });
      google.maps.event.addListener(map, 'click', function(event) {
        var lat, lng;
        lat = event.latLng.lat();
        lng = event.latLng.lng();
        $("#inputMapSearch").val(lat + ", " + lng);
        $("#LngLocation").val(lng);
        return $("#LatLocation").val(lat);
      });
      $lightbox = $('#lightbox');
      $('[data-target="#lightbox"]').on('click', function(event) {
        var $img, alt, css, src;
        $img = $(this).find('gmeg_map_canvas');
        src = $img.attr('src');
        alt = $img.attr('alt');
        css = {
          'maxWidth': $(window).width() - 100,
          'maxHeight': $(window).height() - 100
        };
        $lightbox.find('.close').addClass('hidden');
        $lightbox.find('gmeg_map_canvas').attr('src', src);
        $lightbox.find('gmeg_map_canvas').attr('alt', alt);
        $lightbox.find('gmeg_map_canvas').css(css);
        google.maps.event.trigger(map, 'resize');
      });
      $lightbox.on('shown.bs.modal', function(e) {
        var $img;
        $img = $lightbox.find('gmeg_map_canvas');
        $lightbox.find('.modal-dialog').css({
          'width': $img.width()
        });
        $lightbox.find('.close').removeClass('hidden');
        google.maps.event.trigger(map, 'resize');
      });
      return console.log("DOM is ready");
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
    $scope.getNewFeatureElement = function(counter) {
      var button, copyForm, stuff;
      copyForm = document.getElementById("NeuesFeature");
      stuff = copyForm.cloneNode(true);
      if (counter === void 0) {
        counter = 1;
      }
      stuff.id = "NeuesFeature_" + counter;
      stuff.style.display = "block";
      document.getElementById("Features").appendChild(stuff);
      $("#" + stuff.id).find("#NeuesFeatureFieldset").attr("id", "NeuesFeatureFieldset_" + counter);
      $("#" + stuff.id).find("#btnNeuesFeatureDelete").attr("id", "btnNeuesFeatureDelete_" + counter);
      $("#" + stuff.id).find("#btnFeatureEinklappen").attr("id", "btnFeatureEinklappen_" + counter);
      $("#" + stuff.id).find("#NeuesFeatureContent").attr("id", "NeuesFeatureContent_" + counter);
      $("#" + stuff.id).find("#lblInputFeature").attr("id", "lblInputFeature_" + counter);
      $("#" + stuff.id).find("#inputFeature").attr("id", "inputFeature_" + counter);
      $("#" + stuff.id).find("#btnControlGroup").attr("id", "btnControlGroup_" + counter);
      $("#" + stuff.id).find("#btnCreateInteraction").attr("id", "btnCreateInteraction_" + counter);
      $("#" + stuff.id).find("#ddnInteractions").attr("id", "ddnInteractions_" + counter);
      $("#" + stuff.id).find("#ddnWayChooser").attr("id", "ddnWayChooser_" + counter);
      $("#" + stuff.id).find("#ddnQuiz").attr("id", "ddnQuiz_" + counter);
      $("#" + stuff.id).find("#ddnItem").attr("id", "ddnItem_" + counter);
      $("#lblInputFeature_" + counter).attr("for", "inputFeature_" + counter);
      $("#inputFeature_" + counter).keyup(function() {
        var text;
        text = "Feature: " + $("#inputFeature_" + counter).val();
        return $("#NeuesFeatureFieldset_" + counter).text(text);
      });
      $("#btnNeuesFeatureDelete_" + counter).click(function() {
        if (confirm('Möchten Sie das Feature wirklich löschen?')) {
          return $("#NeuesFeature_" + counter).remove();
        }
      });
      $("#btnFeatureEinklappen_" + counter).click(function() {
        if ($("#NeuesFeatureContent_" + counter).is(":hidden")) {
          $("#NeuesFeatureContent_" + counter).show("slow");
          return $("#btnControlGroup_" + counter).addClass("dropup");
        } else {
          $("#NeuesFeatureContent_" + counter).slideUp("slow");
          return $("#btnControlGroup_" + counter).removeClass("dropup");
        }
      });
      $("#btnCreateInteraction_" + counter).attr("interactionCounter", counter);
      $("#btnCreateInteraction_" + counter).click(function() {
        var interactionCounter;
        if ($("#ddnInteractions_" + counter).val() === "Item") {
          copyForm = document.getElementById("Neu_" + $("#ddnInteractions_" + counter).val());
          interactionCounter = $("#btnCreateInteraction_" + counter).attr("interactionCounter");
          interactionCounter++;
          $("#btnCreateInteraction_" + counter).attr("interactionCounter", interactionCounter);
          stuff = copyForm.cloneNode(true);
          stuff.id = stuff.id + "_" + interactionCounter;
          stuff.style.display = "block";
          document.getElementById("NeuesFeatureContent_" + counter).appendChild(stuff);
          $("#" + stuff.id).find("#NeuesItemFieldset").attr("id", "NeuesItemFieldset_" + interactionCounter);
          $("#" + stuff.id).find("#NeuesItemContent").attr("id", "NeuesItemContent_" + interactionCounter);
          $("#" + stuff.id).find("#btnItemControlGroup").attr("id", "btnItemControlGroup_" + interactionCounter);
          $("#" + stuff.id).find("#btnItemEinklappen").attr("id", "btnItemEinklappen_" + interactionCounter);
          $("#" + stuff.id).find("#btnItemDelete").attr("id", "btnItemDelete_" + interactionCounter);
          $("#" + stuff.id).find("#itemID").attr("id", "itemID_" + interactionCounter);
          $("#" + stuff.id).find("#itemDescription").attr("id", "itemDescription_" + interactionCounter);
          $("#" + stuff.id).find("#itemFeatureRef").attr("id", "itemFeatureRef_" + interactionCounter);
          $("#" + stuff.id).find("#itemIsCollected").attr("id", "itemIsCollected_" + interactionCounter);
          $("#" + stuff.id).find("#lblItemID").attr("id", "lblItemID_" + interactionCounter);
          $("#" + stuff.id).find("#lblItemIsCollected").attr("id", "lblItemIsCollected_" + interactionCounter);
          $("#" + stuff.id).find("#lblItemDescription").attr("id", "lblItemDescription_" + interactionCounter);
          $("#" + stuff.id).find("#lblItemFeatureRef").attr("id", "lblItemFeatureRef_" + interactionCounter);
          $("#lblItemID_" + interactionCounter).attr("for", "itemID_" + interactionCounter);
          $("#lblItemIsCollected_" + interactionCounter).attr("for", "itemIsCollected_" + interactionCounter);
          $("#lblItemDescription_" + interactionCounter).attr("for", "itemDescription_" + interactionCounter);
          $("#lblItemFeatureRef_" + interactionCounter).attr("for", "itemFeatureRef_" + interactionCounter);
          $("#itemFeatureRef_" + interactionCounter).val("NeuesFeature_" + counter);
          $("#btnItemDelete_" + interactionCounter).click(function() {
            if (confirm('Möchten Sie das Item wirklich löschen?')) {
              return $("#Neu_Item_" + interactionCounter).remove();
            }
          });
          $("#btnItemEinklappen_" + interactionCounter).click(function() {
            if ($("#NeuesItemContent_" + interactionCounter).is(":hidden")) {
              $("#NeuesItemContent_" + interactionCounter).show("slow");
              return $("#btnItemControlGroup_" + interactionCounter).addClass("dropup");
            } else {
              $("#NeuesItemContent_" + interactionCounter).slideUp("slow");
              return $("#btnItemControlGroup_" + interactionCounter).removeClass("dropup");
            }
          });
          return $("#itemID_" + interactionCounter).keyup(function() {
            var text;
            text = "Item: " + $("#itemID_" + interactionCounter).val();
            return $("#NeuesItemFieldset_" + interactionCounter).text(text);
          });
        } else if ($("#ddnInteractions_" + counter).val() === "Quiz") {
          copyForm = document.getElementById("Neu_" + $("#ddnInteractions_" + counter).val());
          interactionCounter = $("#btnCreateInteraction_" + counter).attr("interactionCounter");
          interactionCounter++;
          $("#btnCreateInteraction_" + counter).attr("interactionCounter", interactionCounter);
          stuff = copyForm.cloneNode(true);
          stuff.id = stuff.id + "_" + interactionCounter;
          stuff.style.display = "block";
          document.getElementById("NeuesFeatureContent_" + counter).appendChild(stuff);
          $("#" + stuff.id).find("#NeuesQuizFieldset").attr("id", "NeuesQuizFieldset_" + interactionCounter);
          $("#" + stuff.id).find("#NeuesQuizContent").attr("id", "NeuesQuizContent_" + interactionCounter);
          $("#" + stuff.id).find("#btnQuizControlGroup").attr("id", "btnQuizControlGroup_" + interactionCounter);
          $("#" + stuff.id).find("#btnQuizEinklappen").attr("id", "btnQuizEinklappen_" + interactionCounter);
          $("#" + stuff.id).find("#btnQuizDelete").attr("id", "btnQuizDelete_" + interactionCounter);
          $("#" + stuff.id).find("#quizID").attr("id", "quizID_" + interactionCounter);
          $("#" + stuff.id).find("#quizFeatureRef").attr("id", "quizFeatureRef_" + interactionCounter);
          $("#" + stuff.id).find("#quizOnTrue").attr("id", "quizOnTrue_" + interactionCounter);
          $("#" + stuff.id).find("#quizOnFalse").attr("id", "quizOnFalse_" + interactionCounter);
          $("#" + stuff.id).find("#quizQuestion").attr("id", "quizQuestion_" + interactionCounter);
          $("#" + stuff.id).find("#btnQuizAnswer").attr("id", "btnQuizAnswer_" + interactionCounter);
          $("#btnQuizDelete_" + interactionCounter).click(function() {
            if (confirm('Möchten Sie das Quiz wirklich löschen?')) {
              return $("#Neu_Quiz_" + interactionCounter).remove();
            }
          });
          $("#btnQuizEinklappen_" + interactionCounter).click(function() {
            if ($("#NeuesQuizContent_" + interactionCounter).is(":hidden")) {
              $("#NeuesQuizContent_" + interactionCounter).show("slow");
              return $("#btnQuizControlGroup_" + interactionCounter).addClass("dropup");
            } else {
              $("#NeuesQuizContent_" + interactionCounter).slideUp("slow");
              return $("#btnQuizControlGroup_" + interactionCounter).removeClass("dropup");
            }
          });
          $("#quizID_" + interactionCounter).keyup(function() {
            var text;
            text = "Quiz: " + $("#quizID_" + interactionCounter).val();
            return $("#NeuesQuizFieldset_" + interactionCounter).text(text);
          });
          return $("#btnQuizAnswer_" + interactionCounter).click(function() {
            var answer, copyAnswer, quizAnswerCounter;
            quizAnswerCounter = $("#btnQuizAnswer_" + interactionCounter).attr("quizAnswerCounter");
            quizAnswerCounter++;
            $("#btnQuizAnswer_" + interactionCounter).attr("quizAnswerCounter", quizAnswerCounter);
            copyAnswer = document.getElementById("Answer");
            answer = copyAnswer.cloneNode(true);
            answer.id = answer.id + "_" + quizAnswerCounter;
            answer.style.display = "block";
            document.getElementById("NeuesQuizContent_" + interactionCounter).appendChild(answer);
            $("#" + answer.id).find("#lblQuizAnswerID").attr("id", "lblQuizAnswerID_" + quizAnswerCounter);
            $("#" + answer.id).find("#quizAnswerID").attr("id", "quizAnswerID_" + quizAnswerCounter);
            $("#" + answer.id).find("#lblQuizAnswerText").attr("id", "lblQuizAnswerText_" + quizAnswerCounter);
            $("#" + answer.id).find("#quizAnswerText").attr("id", "quizAnswerText_" + quizAnswerCounter);
            $("#" + answer.id).find("#btnQuizAnswerDelete").attr("id", "btnQuizAnswerDelete_" + quizAnswerCounter);
            return $("#btnQuizAnswerDelete_" + quizAnswerCounter).click(function() {
              if (confirm('Möchten Sie die Antwort wirklich löschen?')) {
                return $("#" + answer.id).remove();
              }
            });
          });
        }
      });
      $("#ddnWayChooser_" + counter).click(function() {
        $("#ddnInteractions_" + counter).val("WayChooser");
        return $("#ddnInteractions_" + counter).html("WayChooser <span class='caret'/>");
      });
      $("#ddnQuiz_" + counter).click(function() {
        $("#ddnInteractions_" + counter).val("Quiz");
        return $("#ddnInteractions_" + counter).html("Quiz <span class='caret'/>");
      });
      $("#ddnItem_" + counter).click(function() {
        $("#ddnInteractions_" + counter).val("Item");
        return $("#ddnInteractions_" + counter).html("Item <span class='caret'/>");
      });
      button = document.getElementById("btnFeature");
      button.parentNode.removeChild(button);
      document.getElementById("Features").appendChild(button);
      return button.scrollIntoView(true);
    };
    $scope.getNewPoiElement = function(counter) {
      var button, checkBox1, checkBox2, childs, copyForm, stuff;
      copyForm = document.getElementById("NeuerPOI");
      stuff = copyForm.cloneNode(true);
      stuff.style.display = "block";
      if (counter === void 0) {
        counter = 1;
      }
      stuff.id = "POI_" + counter;
      document.getElementById("POIS").appendChild(stuff);
      childs = document.getElementById(stuff.id).childNodes;
      childs = childs[0].childNodes;
      childs = childs[0].childNodes;
      childs = childs[3].childNodes;
      checkBox1 = childs[0].childNodes;
      checkBox1[0].id = "inputAccessable_" + counter;
      checkBox1[1].id = "lblAccessable_" + counter;
      checkBox1[1].setAttribute("for", "inputAccessable_" + counter);
      checkBox2 = childs[1].childNodes;
      checkBox2[0].id = "inputInternet_" + counter;
      checkBox2[1].id = "lblInternet_" + counter;
      checkBox2[1].setAttribute("for", "inputInternet_" + counter);
      button = document.getElementById("btnPOIS");
      button.parentNode.removeChild(button);
      document.getElementById("POIS").appendChild(button);
      return button.scrollIntoView(true);
    };
    $scope.createInteraction = function() {
      return alert("Your book is overdue.");
    };
    $scope.tabbed_pain = function(activeTabID, activeContentID, passiveTabID, passiveContentID) {
      jQuery(activeTabID).addClass("active");
      jQuery(activeContentID).css(display, "block");
      jQuery(passiveTabID).removeClass("active");
      return jQuery(passiveContentID).css(display, "none");
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
