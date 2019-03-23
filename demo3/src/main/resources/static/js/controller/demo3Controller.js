app.controller('demo3Controller', function($scope, $controller, baseService) {


    $controller('baseController', {$scope: $scope});

    var list = new Array();

    $scope.table={
        "list":JSON.stringify(list),
        "tbComment": "string",
        "tbName": "string"
    };












});