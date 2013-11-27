//
//  ViewController.m
//  KivaJSONDemo
//
//  Created by Paryani, Rajpal on 6/13/13.
//  Copyright (c) 2013 Paryani, Rajpal. All rights reserved.
//

#define kBgQueue dispatch_get_global_queue(
DISPATCH_QUEUE_PRIORITY_DEFAULT, 0) //1
#define kLatestKivaLoansURL [NSURL URLWithString:
@"http://api.kivaws.org/v1/loans/search.json?status=fundraising"] //2

#import "ViewController.h"

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    dispatch_async(kBgQueue, ^{
        NSData* data = [NSData dataWithContentsOfURL:
                        kLatestKivaLoansURL];
        [self performSelectorOnMainThread:@selector(fetchedData:)
                               withObject:data waitUntilDone:YES];
    });
}


- (void)fetchedData:(NSData *)responseData {
    //parse out the json data
    NSError* error;
    NSDictionary* json = [NSJSONSerialization
                          JSONObjectWithData:responseData //1
                          
                          options:kNilOptions
                          error:&error];
    
    NSArray* latestLoans = [json objectForKey:@"loans"]; //2
    
    NSLog(@"loans: %@", latestLoans); //3
}

@end