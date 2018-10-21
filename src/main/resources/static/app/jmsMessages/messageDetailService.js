angular.module('jmsMessageApp')
    .factory('MessageDetail', ['$resource', '$window',
        function($resource) {
            return $resource('getMessage', {}, {
                getMessage: {
                    method: 'POST',
                    interceptor : customInterceptor
                }
            });
        }
    ]);

  
    
    var customInterceptor = {
        request: function(config, $window) {
        	openSpinner();
        	return config;
        },
        response: function(response, $window) {
        	closeSpinner();
        	return response;
        }
      }  
    
    var openSpinner = function() {
        console.log("Open Spinner")
        $('#loaderWrap').show();
        return;
    }

    var closeSpinner = function() {
        console.log("Close Spinner")
        $('#loaderWrap').hide();
        return;
    }