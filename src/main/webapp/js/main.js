var translate;
(function (translate) {
    translate.send = function(url, data) {

        var retries = 3;

        function post() {
            if(!url) {
                throw "url is required.";
            }

            var req = new XMLHttpRequest();
            req.onload = function() {
                if (req.readyState === 4) {
                    if (req.status === 200) {
                      var j = JSON.parse(req.responseText);
                      document.getElementById("targetMessage").value = j.translatedText;
                    } else if (req.status === 500) {
                        retries--;
                        if(retries > 0) {
                            console.log("Retrying...");
                            setTimeout(function(){post()}, 100);
                        } else {
                            console.error("post error");
                            document.getElementById("errorMessage").innerText = "失敗しました。もう一度実行してください。"
                        }
                    }
                }
            };

            try {
                req.open("POST", url, true);
                req.setRequestHeader("Content-Type","application/json;charset=utf-8");
                req.send(JSON.stringify(data));
            } catch(e) {
                throw "Error retrieving data file. Some browsers only accept cross-domain request with HTTP.";
            }
        }
        post();

    };

    translate.submit = function() {
        var sle = document.getElementById("sourceLanguage");
        var sourceLanguage = sle.options[sle.selectedIndex].value;
        
        var tle = document.getElementById("targetLanguage");
        var targetLanguage = tle.options[tle.selectedIndex].value;
        
        var sourceMessage = document.getElementById("sourceMessage").value;
        console.log(sourceMessage);

        var data = {
            "sourceLanguage" : sourceLanguage,
            "targetLanguage" : targetLanguage,
            "message" : sourceMessage,
        };

        document.getElementById("targetMessage").value = "";
        document.getElementById("errorMessage").innerText = "";
        
        translate.send("/api/v1/translate", data);
    };
})(translate || (translate = {}));