var mainApp;

mainApp = angular.module("mainApp", []);

mainApp.controller("mainCtrl", ["$scope", "$http", function($scope, $http) {}]);

mainApp.directive('draggable', function() {
  return function(scope, element) {
    var el;
    return el = element[0];
  };
});
