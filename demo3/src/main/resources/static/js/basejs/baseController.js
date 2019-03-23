/** 基础控制器层 */
app.controller("baseController", function($scope, $http,baseService){

    $scope.paginationConf = {
        currentPage : 1, // 当前页码
        perPageOptions : [10,15,20], // 页码下拉列表框
        totalItems : 0, // 总记录数
        itemsPerPage : 5, // 每页显示记录数
        onChange : function(){ // 当页码发生改变后需要调用的函数
            $scope.reload();
        }
    };
    $scope.reload = function(){
        $scope.search($scope.paginationConf.currentPage,
            $scope.paginationConf.itemsPerPage);
    };

    $scope.address={};
    $scope.order={};
    $scope.loadCoSeller = function(){
        baseService.sendGet("/login/loadCoSeller")
            .then(function (response) {
              if(response.data.data){
                    $scope.coSeller=response.data.data;
              }else if(response.data.message){
                  alert(response.data.message)
              }
            },function () {
                alert("服务器没响应！")
            });
    };

    $scope.login=function(){
        window.location.href="/login.html";
    };
    $scope.userLoginout=function(){
        baseService.sendGet("/user/userLoginout")
            .then(function () {
                $scope.user=null;
                alert("退出成功！")
            },function () {
                alert("退出失败！")
            })
    };

    $scope.getCookieValue=function(name){
        var cookiestr = document.cookie;
        var arr = cookiestr.split('; ');
        for(var i = 0; i < arr.length; i++){
            var temp = arr[i].split('=');
            if(temp[0] == name){
                return temp[1];
            }
        }
        return '';
    };
    $scope.setCookie = function (name, value, day) {
        if (day !== 0) {
            //当设置的时间等于0时，不设置expires属性，cookie在浏览器关闭后删除
            var expires = day * 24 * 60 * 60 * 1000;
            var date = new Date(+new Date() + expires);
            document.cookie = name + "=" + escape(value) + ";expires=" + date.toUTCString();
        } else {
            document.cookie = name + "=" + escape(value);
        }
    };
    $scope.ids = [];
    $scope.change={};
    $scope.updateSelection = function($event,emp){
        if ($event.target.checked){
            $scope.ids.push(emp.id);
        }else{
            var idx = $scope.ids.indexOf(emp.id);
            $scope.ids.splice(idx,1);
        }
        $scope.change=emp;
    };
    $scope.validatePhone=function (phone) {
        if ((/(^1(3|4|5|7|8)\d{9}$)/.test(phone))) {
            return true;
        }
        return false;
    };
    $scope.belongNum=function (num) {
        if(/-?[1-9]\d*$/.test(num)){
            return true;
        }
        return false;

    };
    $scope.belongNegativeNum=function (num) {
        if(/-[1-9]\d*$/.test(num)){
            return true
        }
        return false;
    }
    /*$scope.Jconcat=function(json1,json2){
        var resultJsonObject = {};
        for (var attr in json1) {
            resultJsonObject[attr] = json1[attr];
        }
        for (  attr in json2) {
            resultJsonObject[attr] = json2[attr];
        }
        return resultJsonObject;
    };

    $scope.Jdelete=function(json1,json2){

        for(var i=0;i<json2.length;i++){
          for(var j=0;j<json1.length;i++){
              if(json1[j]=json2[i]){
                  delete json1[j][json2[i]];
              }
          }
        }
        return json1;
    };*/
});