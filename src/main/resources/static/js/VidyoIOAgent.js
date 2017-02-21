(function() {
    var parentViewId = "";

    var renderToDiv = "";
    var resourceId = "";
    var token = "";
    var userJoinedCallback = function () {
    };
    var agentJoinedCallback = function () {
    };
    var agentLeftCallback = function () {
    };
    var autoDisconnectCountdown;
    var autoDisconnectSecondsCounter = 30;

    function loadScript(scriptUrl) {
        var dynamicScript = document.createElement("script");
        dynamicScript.src = scriptUrl;
        document.getElementsByTagName('head')[0].appendChild(dynamicScript);
    }

    function vidyoIOAgent(config) {
        parentViewId = config.viewId;
        if (config.onUserJoined != undefined) {
            userJoinedCallback = config.onUserJoined;
        }
        if (config.onAgentJoined != undefined) {
            agentJoinedCallback = config.onAgentJoined;
        }
        if (config.onAgentLeft != undefined) {
            agentLeftCallback = config.onAgentLeft;
        }

        var msgBar = document.createElement("div");
        msgBar.style.backgroundColor = "black";
        msgBar.style.color = "white";
        msgBar.style.textAlign = "center";
        msgBar.style.fontWeight = "bold";
        msgBar.style.fontFamily = "sans-serif";
        msgBar.id = "vidyoIOMsgBar";
        msgBar.appendChild(document.createTextNode("Loading..."));

        document.getElementById(parentViewId).appendChild(msgBar);
        document.getElementById(parentViewId).style.backgroundColor = "black";
        var videoDiv = document.createElement("div");
        videoDiv.id = "vidyoIORender";
        videoDiv.style.width = "100%";
        videoDiv.style.height = "100%";
        document.getElementById(parentViewId).appendChild(videoDiv);
        document.getElementById(parentViewId).style.display = "block";
        renderToDiv = "vidyoIORender";

        loadScript(config.appUrl + "/callme/" + config.appKey + "?callback=startConference");
    }

    function showMessage(text) {
        console.log(text);
        if (document.getElementById("vidyoIOMsgBar") != undefined) {
            document.getElementById("vidyoIOMsgBar").textContent = text;
        }
    }

    function startConference(response) {
        if (response.error == "forbidden") {
            showMessage("Not configured or forbidden.");
            return;
        }
        resourceId = response.room;
        token = response.token;
        //loadScript("https://static.vidyo.io/latest/javascript/VidyoClient/VidyoClient.js?onload=onVidyoClientLoaded&webrtc=true");
        loadScript("https://static.vidyo.io/latest/javascript/VidyoClient/VidyoClient.js?onload=onVidyoClientLoaded");
    }

    function onVidyoClientLoaded(status) {
        console.log("Status: " + status.state + "Description: " + status.description);
        switch (status.state) {
            case "READY":    // The library is operating normally
                // After the VidyoClient is successfully initialized a global VC object will become available
                // All of the VidyoConnector gui and logic is implemented in VidyoConnector.js
                startVidyoConnector(VC);
                break;
            case "RETRYING": // The library operating is temporarily paused
                break;
            case "FAILED":   // The library operating has stopped
                showMessage("Failed: " + status.description);
                break;
            case "FAILEDVERSION":   // The library operating has stopped
                var overlay = createOverlayWithMessage("vidyoErrorOverlay", "Download new version of plugin <a href='" + status.downloadPathPlugIn + "'>here</a> and restart your browser.");
                document.getElementById("vidyoIOMsgBar").appendChild(overlay);
                break;
            case "NOTAVAILABLE": // The library is not available

                if (status.downloadType == "PLUGIN") {
                    var overlay = createOverlayWithMessage("vidyoErrorOverlay", "Download plugin <a href='" + status.downloadPathPlugIn + "'>here</a> and restart your browser.");
                    document.getElementById("vidyoIOMsgBar").appendChild(overlay);
                } else {
                    showMessage(status.description);
                    var overlay = createOverlayWithMessage("vidyoErrorOverlay", "Your browser is not supported (use Chrome or Firefox). Click here to dismiss.");
                    document.getElementById("vidyoIOMsgBar").appendChild(overlay);
                    overlay.onclick = function (event) {
                        if (document.getElementById("vidyoErrorOverlay") != undefined) {
                            document.getElementById(parentViewId).innerHTML = "";
                            document.getElementById(parentViewId).style.display = "none";
                        }
                    };
                }
                break;
        }
    }

    function startVidyoConnector(VC) {
        VC.CreateVidyoConnector({
            viewId: renderToDiv, 		// Div ID where the composited video will be rendered, see VidyoConnectorSample.html
            viewStyle: "VIDYO_CONNECTORVIEWSTYLE_Tiles", // Visual style of the composited renderer
            remoteParticipants: 2,     // Maximum number of participants
            logFileFilter: "warning all@VidyoConnector info@VidyoClient",
            logFileName: "",
            userData: ""
        }).then(function (vc) {
            vidyoConnector = vc;
            connectToConference(vidyoConnector);
            showMessage("Waiting for agent...");
        }).catch(function (err) {
            showMessage("Failed");
            console.error("CreateVidyoConnector Failed " + err);
        });
    }

    function connectToConference(vidyoConnector) {

        console.log("CONNECTING...");

        vidyoConnector.Connect({
            // Take input from options form
            host: "prod.vidyo.io",
            token: token,
            displayName: "Customer",
            resourceId: resourceId,

            onSuccess: function () {
                console.log("connect success");
                userJoinedCallback();
                handleParticipantChange(vidyoConnector);
            },
            onFailure: function (reason) {
                console.log("failed: " + reason);
            },
            onDisconnected: function (reason) {
                console.log("Call Disconnected: " + reason);
            }
        }).then(function (status) {
            if (status) {
                console.log("ConnectCall Success");
            } else {
                console.error("ConnectCall Failed");
            }
        }).catch(function () {
            console.error("ConnectCall Failed");
        });
    }

    function createOverlayWithMessage(id, msg) {
        var overlay = document.createElement("div");
        overlay.style.backgroundColor = "black";
        overlay.style.color = "white";
        overlay.style.textAlign = "center";
        overlay.style.fontWeight = "bold";
        overlay.style.fontFamily = "sans-serif";
        overlay.style.fontSize = "24px";
        overlay.style.width = "100%";
        overlay.style.position = "absolute";
        overlay.style.bottom = "0";
        overlay.style.paddingTop = "200px";
        overlay.style.top = "0";
        overlay.style.opacity = "0.5";
        overlay.style.zIndex = "999";
        overlay.style.cursor = "pointer";
        overlay.id = id;
        //overlay.appendChild(document.createTextNode(msg));
        overlay.innerHTML = msg;
        return overlay;
    }

    function showDisconnectOverlay() {
        var overlay = createOverlayWithMessage("vidyoIOOverlay", "Click to here disconnect");
        overlay.onclick = function (event) {
            hideDisconnectOverlay();
            disconnectCall()
        };

        document.getElementById("vidyoIOMsgBar").appendChild(overlay);
        startAutoDisconnectTimer();
    }

    function disconnectCall() {
        vidyoConnector.Disconnect().then(function () {
            console.log("Client disconnected");
            document.getElementById(parentViewId).innerHTML = "";
            document.getElementById(parentViewId).style.display = "none";
        }).catch(function () {
            console.error("Disconnect Failure");
        });
    }

    function hideDisconnectOverlay() {
        stopAutoDisconnectTimer();
        if (document.getElementById("vidyoIOOverlay") != undefined) {
            document.getElementById("vidyoIOMsgBar").removeChild(document.getElementById("vidyoIOOverlay"));
        }
    }


    function startAutoDisconnectTimer() {
        autoDisconnectCountdown = setInterval(function () {
            document.getElementById("vidyoIOOverlay").innerHTML = "Click to here disconnect<br/>Autodisconnect in " + autoDisconnectSecondsCounter-- + " seconds";
            if (autoDisconnectSecondsCounter <= 0) {
                stopAutoDisconnectTimer();
                disconnectCall();
            }
        }, 1000);
    }

    function stopAutoDisconnectTimer() {
        clearInterval(autoDisconnectCountdown);
        autoDisconnectSecondsCounter = 30;
    }

    function handleParticipantChange(vidyoConnector) {
        vidyoConnector.RegisterParticipantEventListener({
            onJoined: function (participant) {
                showMessage("Agent connected.");
                hideDisconnectOverlay();
                agentJoinedCallback();
            },
            onLeft: function (participant) {
                showMessage("Agent disconnected.");
                agentLeftCallback();
                showDisconnectOverlay();
            },
            onDynamicChanged: function (participants, cameras) {
                // Order of participants changed
            },
            onLoudestChanged: function (participant, audioOnly) {

            }
        }).then(function () {
            console.log("RegisterParticipantEventListener Success");
        }).catch(function () {
            console.err("RegisterParticipantEventListener Failed");
        });
    }

    window.vidyoIOAgent = vidyoIOAgent;
    window.startConference = startConference;
    window.onVidyoClientLoaded = onVidyoClientLoaded;
}());
