$(function(){

    var showBase = "/show?type=";
    var listURI = "/list";
    var showTypesURI = "/showTypes?";
    var disableURI = "/disable?type=";
    var enableURI = "/enable?type=";
    var resetURI = "/reset?type=";


    var tplRow = $('#dataRow');
    var rowContainer = $('#rowBody');

    function buildShowTypesParameters(types) {
        return 'type=' + types.join("&type=");
    }

    function newRow() {
        var newRow = tplRow.clone();
        rowContainer.append(newRow);
        return newRow;
    }

    var watchMap = {};
    var watching = false;
    var timer;

    function nsToMs(v) {
        return v / 1000000;
    }

    var fomatter = {
        "timeConsumed" : nsToMs,
        "minElapsed" : nsToMs,
        "lastElapsed" : nsToMs,
        "maxElapsed" : nsToMs
        };


    function renderRow(val, row) {
        $.each(val, function(k,v) {
                if (v === 9223372036854776000 
                    || v === -9223372036854776000) {
                   v = "N/A"; 
                } else {
                   var ft = fomatter[k];
                   if (ft) v = ft(v);
                }
               $('.' + k, row).text(v);
        });
    }

    var showData = [];
    var xAxis = 0;
    var options = {
        lines: { show: true },
        points: { show: true },
        xaxis: { show: false, tickDecimals: 0, tickSize: 1,  ticks: false }
    };
            
    var series = {};
    
    function renderMetric(resMap) {
        $.each(resMap, function(k,v){
            var row = watchMap[k];
            if (row != null) {
                if (!series[k]) {
                    series[k] = [];
                }

                var _data = series[k];
                if (_data.length > 15) {
                    _data.shift();
                }
                var val = nsToMs(v["lastElapsed"]);
                _data.push([xAxis++, val]);
                renderRow(v, row);
            }
        });


        var _s = [];
        $.each(series,function(k,v) { //ugly
            _s.push({label:k, data : v});
        });
    
        //$.plot($("#ph"), _s, options);
    }

    function fetchMetric() {
        console.log("fetching");
        var types = [];
        $.each(watchMap, function(k) {
            types.push(k);
        });
        var qryString = buildShowTypesParameters(types);
        var url = showTypesURI + qryString; 
        $.getJSON(url, renderMetric);
    }

    function startWatch() {
       timer = setInterval(fetchMetric, 2 * 1000);  
       watching = true;
    }

    var cats = {};

    function stopWatch() {
        clearInterval(timer);
        watching = false;
    }

    function stopAll() {
        $.each(cats,function(k,v){
            removeWatch(k, v);
            console.log("rm " + k);
        });
    }

    function addWatch(cat, row) {
        watchMap[cat] = row;
        $('.watch', row).text("stopWatch").addClass('stopWatch');
        if (! watching) {
            startWatch();
        }
    }


    function watchAll(){
        $.each(cats,function(k,v){
            addWatch(k, v);
        });
    }

    function objSize(obj) {
        var size = 0, key;
        for (key in obj) { if (obj.hasOwnProperty(key)) size++; }
        return size;
    }

    function removeWatch(cat, row) {
        delete watchMap[cat];
        $('.watch', row).text("watch").removeClass('stopWatch');
        if (objSize(watchMap) == 0) {
            stopWatch();
        }
    }


    $.ajax({
       dataType : "json",
       url: listURI,
       success: function(msg){ 
           console.log(msg);
           $.each(msg, function(i,v) {
               //cats = v;
               var tag = v;
               console.log(v);
               var r = newRow();
               $('.Name', r).text(v);
               r.show();
               cats[v] = r;

               //todo disable buttons before we got ajax callbacks
               //watch handler
               $('.watch', r).click(function(){
                  var theRow = r; //todo
                  var self = $(this);
                  if (self.hasClass('stopWatch')) {
                      removeWatch(v,theRow);
                      //self.text("watch").removeClass('stopWatch');
                  } else {
                      addWatch(v, theRow);
                      //self.text("stop watch").addClass('stopWatch');
                  }
               });

               //disable handler
               $('.disableAct', r).click(function(){
                  var theRow = r; //todo
                  var self = $(this);
                  if (self.hasClass('_disabled')) {
                      $.get("/enable?type=" + v);
                      self.text('disable').removeClass('_disabled');
                  } else {
                      $.get("/disable?type=" + v);
                      self.text("enable").addClass('_disabled');
                  }
               });

               //reset handler
               $('.reset', r).click(function(){
                  var theRow = r; 
                  var self = $(this);
                  $.get("/reset?type=" + v);
               });



           });
       }
    });

    //$('#stop').click(stopWatch);
    $('#watchToggle').click(function(){
        var self = $(this);
        if (self.hasClass("watching")) {
            stopAll();
            self.text("watchAll").removeClass("watching");
        } else {
            watchAll();
            self.addClass("watching").text("stopAll");
        }
    });

});

