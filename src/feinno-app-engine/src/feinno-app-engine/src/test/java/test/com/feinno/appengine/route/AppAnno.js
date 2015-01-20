{
    "classInfo" :
    {
	 	"type": "test.com.feinno.appengine.AppBeanExample",
	 	"baseClass": 
		{
	       type: "com.feinno.appengine.RemoteAppBean",
		    genericParams: 
		    [
		        { 
			      "key" : "A",
	             "value" : "test.com.feinno.appengine.SampleArgs"
				},
		        { 
			    "key" : "R",
	           "value" : "test.com.feinno.appengine.SampleResults"
				},
		        { 
			    "key" : "C",
	           "value" : "test.com.feinno.appengine.SampleContext"
				}
		    ]	    
		}
    },
    "annotations": 
    [
		{
		    "type" : "com.feinno.appengine.http.HttpPrefix",
		    "fields" : 
			 [
			        {
		                    "key" : "value",
		                    "value" : "/login.aspx"
					},
					{
				    			"key" : "name",
		                    "value" : "SingleSignIn"
					}
			 ]
		},
		{
		    "type" : "com.feinno.annotation.Stateful",
	       "fields": 
		    [
		        {
                    "key" : "value",
                    "value" : "Presence"
				}		
		    ]
		}
    ]
}