app.service('uploadService', function($http){

    /** 定义文件异步上传的方法 */
    this.uploadExcelFile = function(){

        // 创建表单数据对象
        var formData = new FormData();
        // 表单数据对象追加上传的文件
        // 第一个参数：请求参数名 <input type="file" name='file'/>
        // 第二个参数：取html页面中第一个file
        formData.append("file", file.files[0]);
        if(file.name) {
            var fileName = file.name.substring(file.name.lastIndexOf(".") + 1);
            if (fileName == "xlsx" || fileName == "xls") {
                formData.append('file', file);
                $http({
                    method: "post",
                    url: "/index/uploadExcelFile",
                    data: formData,
                    headers: {'Content-Type': "multipart/form-data"},
                    transformRequest: angular.identity
                }).then(function (response) {
                    if (response.status == 200) {
                        alert("文件上传成功！！！");
                    } else {
                        alert("文件上传失败！！！");
                    }
                });
            } else {
                alert("文件格式不正确，请上传以.xlsx 或 .xls为后缀的文件。");
                $("#file").val("");
            }

        }

    };
    this.uploadFile=function () {

        var formData = new FormData();
        formData.append("file",file.files[0]);
        return $http({
            method : 'post',
            // 请求URL
            url : '/index/uploadExcelFile',
            // 表单数据对象
            data : formData,
            headers : {"Content-Type":undefined}, // 设置请求头
            transFormRequest : angular.identity // 转换表单的请求对象(把文件转化成字节)
        }).then(function (response) {
            if(response.data.flag==true){
                alert("上传成功!");
            }else  if(response.data.message){
                alert(response.data.message);
            }else{
                alert('未知失败！');
            }
        },function () {
            alert('服务器没响应！');
        });
    };
    /**
     * 图片上传数量及其大小等控制
     * 点击开始上传按钮(test9)，执行上传
     */
    /*var success=0;
    var fail=0;
    var imgurls="";

    $(function (){
        var imgsName="";
        layui.use(['upload','layer'],function() {
            var upload = layui.upload;
            var layer=layui.layer;

            upload.render({
                elem: '#test1',
                url: '/upload',
                multiple: true,
                auto:false,
//			上传的单个图片大小
                size:10240,
//			最多上传的数量
                number:20,
//			MultipartFile file 对应，layui默认就是file,要改动则相应改动
                field:'file',
                bindAction: '#test9',
                before: function(obj) {
                    //预读本地文件示例，不支持ie8
                    obj.preview(function(index, file, result) {
                        $('#demo2').append('<img src="' + result
                            + '" alt="' + file.name
                            +'"height="92px" width="92px" class="layui-upload-img uploadI mgPreView">')
                    });
                },
                done: function(res, index, upload) {
                    //每个图片上传结束的回调，成功的话，就把新图片的名字保存起来，作为数据提交
                    console.log(res.code);
                    if(res.code=="1"){
                        fail++;
                    }else{
                        success++;
                        imgurls=imgurls+""+res.data.src+",";
                        $('#imgUrls').val(imgurls);
                    }
                },
                allDone:function(obj){
                    layer.msg("总共要上传图片总数为："+(fail+success)+"\n"
                        +"其中上传成功图片数为："+success+"\n"
                        +"其中上传失败图片数为："+fail
                    )
                }
            });

        });

        //清空预览图片
        cleanImgsPreview();
        //保存商品
        goodsSave();
    });

    /!**
     * 清空预览的图片
     * 原因：如果已经存在预览的图片的话，再次点击上选择图片时，预览图片会不断累加
     * 表面上做上传成功的个数清0
     *!/
    function cleanImgsPreview(){
        $("#cleanImgs").click(function(){
            success=0;
            fail=0;
            $('#demo2').html("");
            $('#imgUrls').val("");
        });
    }

    /!**
     * 保存商品
     *!/
    function goodsSave(){
        $('#btnSubmit').click(function(){
            var tt=$("#title").val();
            var st=$("#smallTit").val();
            var ius=$("#imgUrls").val();

            $.ajax({
                type: "POST",
                url: "/saveGoods",
                data: {
                    title:tt,
                    smallTit:st,
                    imgUrls:ius,
                },
                success: function(msg){
                    if(msg=="1"){
                        alert("保存成功");
                    }else{
                        alert("保存失败");
                    }
                }
            });
        });
    }*/
});