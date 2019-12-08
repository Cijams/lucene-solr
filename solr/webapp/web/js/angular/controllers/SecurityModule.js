/*
    Custom API key for function calls. Can be replace by user or in the config file.
*/
var APIKEY = '1fe077453818c828eaa3981bea7e9465f1366ee2bffa545791e3de59dc27f80a'

/*
    Function displays the name of the uploaded document to the user.
*/
function showname() {
    var pathToFile = document.getElementById('fileInput');
    $("#scanMe").text(name.files.item(0).pathToFile);
};

/*
    Uploads the selected file to the virus scanner for remote evaluation.
    Requires each user have an API key. API keys are freely available from
    virustotal.com for all users.
*/
function virusScan() {
    $.getJSON('https://www.virustotal.com/vtapi/v2/file/scan&apikey=APIKEY&file=pathToFile', function (data) {
        $.each(data, function (key, val) {
            myKeys.push(key)
            myValues.push(val)
        });
        console.log(myKays, myValues);
    });
    $("#fileScanned").text("File sent to server for remote evaluation.");
}

/*
    Generates the report from virustotal.com. Over 50 separate security vendors are queried for input
    to detect malicious content of the file. Includes malware and security vulnerabilities.
*/
function getReport() {
    // Retrieve the information.
    var x = document.getElementById("fileScanned");
    x.style.display = "none";
    var x = document.getElementById("report");
    x.style.display = "block";
    let virusScanners = "";
    $.getJSON('https://www.virustotal.com/vtapi/v2/file/report?apikey=APIKEY&resource=5ec4825dab0e8a0ec8580c3ecc3dc8409e1a06d15d34621bc3d10d1800a40810', function (data) {
        var items = [];
        var myKeys = [];
        var myValues = [];
        $.each(data, function (key, val) {
            items.push("<li id='" + key + "'>" + val + "</li>");
            myKeys.push(key)
            myValues.push(val)
        });
        console.log(items);

        // List of security vendors scanned:
        for (var k in myValues[0]) {
            virusScanners = virusScanners + " | " + k;
        }
        virusScanners = virusScanners.substring(3);
        $("#scanners").text(virusScanners);

        // Scan Identification Hash.
        $("#scanId").text(myValues[1]);

        // Scan sha-1 Hash.
        $("#sha-1").text(myValues[2]);

        // Resource Identification number.
        $("#resourceID").text(myValues[3]);

        // Response Code.
        $("#responseCode").text(myValues[4]);

        // Scan Date.
        $("#scanDate").text(myValues[5]);

        // Sha-256.
        $("#sha-256").text(myValues[10]);

        // md5.
        $("#md-5").text(myValues[11]);

        // Total.
        $("#total").text(myValues[9]);

        // Update web display.
        $("<ul/>", {
            "class": "my-new-list",
            html: items.join("")
        }).appendTo("body");
    });
}