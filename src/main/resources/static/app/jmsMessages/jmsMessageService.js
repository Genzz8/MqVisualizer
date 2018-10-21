angular.module('jmsMessageApp')
    .factory('Messages', ['$resource', '$window',
        function($resource) {
            return $resource('getMessageTest', {}, {
                query: {
                    method: 'GET',
                    isArray:true,
                    interceptor : customInterceptor
                },

                getMessages: {
                    method: 'POST',
                    isArray:true,
                    interceptor : customInterceptor
                },
                
                getMessage: {
                    method: 'POST',
                    interceptor : customInterceptor
                }
            });
        }
    ]);

    var openSpinner = function() {
        $('#loaderWrap').show();
        return;
    }

    var closeSpinner = function() {
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