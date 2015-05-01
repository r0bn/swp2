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
      stuff.style.display = "block";
      if (counter === void 0) {
        counter = 1;
      }
      stuff.id = "NeuesFeature_" + counter;
      document.getElementById("Features").appendChild(stuff);
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
