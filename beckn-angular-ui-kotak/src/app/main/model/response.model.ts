export interface ResponseRet<T>{
     status:string;
     message:T;
}


export interface Graph<T>{
     x:string;
     y:T;
}



export interface BuyerSellerResponse{
     buyerId:string;
     sellerId:string;
     count:number;
}


export interface FileRespons {
     fileName: string;
     blob: string;
     fileType: string;
   }
 
   