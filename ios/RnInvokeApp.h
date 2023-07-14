
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNRnInvokeAppSpec.h"

@interface RnInvokeApp : NSObject <NativeRnInvokeAppSpec>
#else
#import <React/RCTBridgeModule.h>

@interface RnInvokeApp : NSObject <RCTBridgeModule>
#endif

@end
