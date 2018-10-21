angular.module('jmsMessageApp')
    .factory('DetailService', ['$resource', '$window',
        function($resource) {
            return $resource('getMessage', {}, {
                getMessage: {
                    method: 'POST',
                    interceptor : customInterceptor
                },
                deleteMessage: {
                	method: 'DELETE',
                	interceptor: customInterceptor
                }
            });
        }
    ]);

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