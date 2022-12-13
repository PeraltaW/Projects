#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<math.h>


int c_miss, c_hit, mem_write, mem_read; 
struct cached** cache;
unsigned count;

struct cached{
unsigned long tag;
int valid;
unsigned long time;
};

struct cached** cacheGenerator(int setnum,int assoc)
{
  int i,j;
  cache=(struct cached**)malloc(setnum*sizeof(struct cached*));
for(i=0;i<setnum;i++)
{
    cache[i]=(struct cached*)malloc((assoc)*sizeof(struct cached));
}
  for(i=0;i<setnum;i++)
	{
    for(j=0;j<assoc;j++)
 	 {
         cache[i][j].valid=0;
         }
        }
return cache;
}

void reading(unsigned long  indextag,unsigned long  setindex,int assoc){

int i,j,min;

for(i=0;i<assoc;i++)
{
if(cache[setindex][i].valid==0)
 {
  c_miss++;
  mem_read++;
  count++;
  cache[setindex][i].valid=1;
  cache[setindex][i].tag=indextag;
  cache[setindex][i].time=count;
 return;
 }
else {
  if(cache[setindex][i].tag==indextag)
  {
	c_hit++;
	return;
  }
if(i==(assoc-1))
{
c_miss++;
mem_read++;
min=0;
for(j=0;j<assoc;j++)
{
   if(cache[setindex][j].time<=cache[setindex][min].time)
	{
	min=j;
	}	
}
		cache[setindex][min].valid=1;
	        cache[setindex][min].tag=indextag;
	        count++;
	        cache[setindex][min].time=count;
	        return;
	}
 }
}
return;
}
void writing(unsigned long  indextag,unsigned long  setindex,int assoc)
{
int i,j,min;

for(i=0;i<assoc;i++)
{
  if(cache[setindex][i].valid==0)
  {
	c_miss++;
	mem_read++;
	mem_write++;
	count++;
	cache[setindex][i].valid=1;
	cache[setindex][i].tag=indextag;
	cache[setindex][i].time=count;
   return;
   }
else
{
 if(cache[setindex][i].tag==indextag)
	{
	c_hit++;
	mem_write++;
	return;
	}
 if(i==(assoc-1))
	{
	c_miss++;
	mem_read++;
	mem_write++;
	min=0;
for(j=0;j<assoc;j++)
 {
 if(cache[setindex][j].time<=cache[setindex][min].time)
	{
	min=j;
	}	
 }	
		cache[setindex][min].valid=1;
	        cache[setindex][min].tag=indextag;
	        count++;
	        cache[setindex][min].time=count;
	        return;
}
		}
}
return;
}

void clear(int setnum, int assoc)
{
 int i,j;
for(i=0;i<setnum;i++)
 {
  for(j=0;j<assoc;j++)
  {
  cache[i][j].tag=0;
  cache[i][j].valid=0;
  cache[i][j].time=0;
  }
 }
 c_miss=0;
 c_hit=0;
 mem_read=0;
 mem_write=0;
 count=0;
}

void write_prefetch(unsigned long  indextag,unsigned long  setindex,int assoc)
{
 int i,j,min;
for(i=0;i<assoc;i++)
 {
 if(cache[setindex][i].valid==0)
  {
	mem_read++;
	count++;
	cache[setindex][i].valid=1;
	cache[setindex][i].tag=indextag;
	cache[setindex][i].time=count;
	return;
  }
else
 {
    if(cache[setindex][i].tag==indextag)
  {
	return;
  }
 if(i==(assoc-1))
 {
 mem_read++;
 min=0;
for(j=0;j<assoc;j++)
{
 if(cache[setindex][j].time<=cache[setindex][min].time)
  {
 min=j;
  }	
 }
		cache[setindex][min].valid=1;
	        cache[setindex][min].tag=indextag;
	        count++;
	        cache[setindex][min].time=count;
	        return;
 }
}		
}

return;
}

void read_prefetchr(unsigned long  indextag,unsigned long  setindex,int assoc)
{
 int i,j,min;
for(i=0;i<assoc;i++)
 {
 if(cache[setindex][i].valid==0)
  {
	mem_read++;
	count++;
	cache[setindex][i].valid=1;
	cache[setindex][i].tag=indextag;
	cache[setindex][i].time=count;

	return;
  }
else
  {
 if(cache[setindex][i].tag==indextag)
  {
 return;
  }
if(i==(assoc-1)){

mem_read++;
min=0;
for(j=0;j<assoc;j++)
{
	if(cache[setindex][j].time<=cache[setindex][min].time)
	{
 	min=j;
	}	
}		cache[setindex][min].valid=1;
	        cache[setindex][min].tag=indextag;
	        count++;
	        cache[setindex][min].time=count;
	        return;
}
 }
}

}
void write_prefetcher(unsigned long  indextag,unsigned long  setindex,int assoc,unsigned long  indextagn,unsigned long  setindexn)
{	
int i,j,min;
for(i=0;i<assoc;i++)
{
  if(cache[setindex][i].valid==0)
	{
	c_miss++;
	mem_read++;
	mem_write++;
	count++;
	cache[setindex][i].valid=1;
	cache[setindex][i].tag=indextag;
	cache[setindex][i].time=count;
	write_prefetch(indextagn,setindexn,assoc);
	return;
	}
else
{
 if(cache[setindex][i].tag==indextag)
 {
	c_hit++;
	mem_write++;
	return;
 }
 if(i==(assoc-1))
 {
     	c_miss++;
	mem_read++;
	mem_write++;
	min=0;
      for(j=0;j<assoc;j++)
	{
	if(cache[setindex][j].time<=cache[setindex][min].time)
		{
				min=j;
		}	
	}			
		cache[setindex][min].valid=1;
	        cache[setindex][min].tag=indextag;
	        count++;
	        cache[setindex][min].time=count;
	        write_prefetch(indextagn, setindexn,assoc);
	        return;
 }

 }
}
return;
}

void read_prefetch(unsigned long  indextag,unsigned long  setindex,int assoc,unsigned long  indextagn,unsigned long  setindexn){

int i,j,min;
for(i=0;i<assoc;i++)
 {
  if(cache[setindex][i].valid==0)
   {
	c_miss++;
	mem_read++;
	count++;
	cache[setindex][i].valid=1;
	cache[setindex][i].tag=indextag;
	cache[setindex][i].time=count;
	read_prefetchr(indextagn,setindexn,assoc);
   return;
    }
else
 {
   if(cache[setindex][i].tag==indextag)
	{
	c_hit++;
	return;
	}
   if(i==(assoc-1))
	{
	c_miss++;
	mem_read++;
	min=0;
  for(j=0;j<assoc;j++)
	{
	if(cache[setindex][j].time<=cache[setindex][min].time)
	{
	min=j;
	}	
}	
		cache[setindex][min].valid=1;
	        cache[setindex][min].tag=indextag;
	        count++;
	        cache[setindex][min].time=count;
	        read_prefetchr(indextagn,setindexn,assoc);
	        return;
	}
}

}
return;
}

void readingl(unsigned long  indextag,unsigned long  setindex,int assoc)
{

int i,j,min;

for(i=0;i<assoc;i++)
{
	if(cache[setindex][i].valid==0)
	{
	c_miss++;
	mem_read++;
	count++;
	cache[setindex][i].valid=1;
	cache[setindex][i].tag=indextag;
	cache[setindex][i].time=count;
	return;
	}
else
	{
	if(cache[setindex][i].tag==indextag)
	  {
			c_hit++;
			count++;
			cache[setindex][i].time=count;
			return;
	  }
if(i==(assoc-1))
	{
	c_miss++;
	mem_read++;
	min=0;
	for(j=0;j<assoc;j++)
	{
	if(cache[setindex][j].time<=cache[setindex][min].time)
	 {
				min=j;
	 }	
	}
			
		cache[setindex][min].valid=1;
	        cache[setindex][min].tag=indextag;
	        count++;
	        cache[setindex][min].time=count;
return;
	}
  }
}

return;
}

void writingl(unsigned long  indextag,unsigned long  setindex,int assoc){

int i,j,min;
for(i=0;i<assoc;i++)
{
	if(cache[setindex][i].valid==0)
	{
	c_miss++;
	mem_read++;
	mem_write++;
	count++;
	cache[setindex][i].valid=1;
	cache[setindex][i].tag=indextag;
	cache[setindex][i].time=count;
	return;
	}
 else{
   if(cache[setindex][i].tag==indextag)
	{
			c_hit++;
			mem_write++;
			count++;
			cache[setindex][i].time=count;
			return;
	}
  if(i==(assoc-1))
	{
			c_miss++;
			mem_read++;
			mem_write++;
			min=0;
			for(j=0;j<assoc;j++)
	{
  if(cache[setindex][j].time<=cache[setindex][min].time)
	{
				min=j;
	}	
	}
		cache[setindex][min].valid=1;
	        cache[setindex][min].tag=indextag;
	        count++;
	        cache[setindex][min].time=count;
	        return;
  }
}
}
return;

}
	
void write_prefetchl(unsigned long  indextag,unsigned long  setindex,int assoc,unsigned long  indextagn,unsigned long  setindexn)
{
	
int i,j,min;
for(i=0;i<assoc;i++)
{
	if(cache[setindex][i].valid==0)
	{
	c_miss++;
	mem_read++;
	mem_write++;
	count++;
	cache[setindex][i].valid=1;
	cache[setindex][i].tag=indextag;
	cache[setindex][i].time=count;
	write_prefetch(indextagn,setindexn,assoc);
	return;
	}
else{
	if(cache[setindex][i].tag==indextag)
	{
			c_hit++;
			mem_write++;
			count++;
			cache[setindex][i].time=count;
			return;
	}
	if(i==(assoc-1))
	{
			c_miss++;
			mem_read++;
			mem_write++;
			min=0;
			for(j=0;j<assoc;j++)
	{
	if(cache[setindex][j].time<=cache[setindex][min].time)
	{
	min=j;
}	
}		
		cache[setindex][min].valid=1;
	        cache[setindex][min].tag=indextag;
	        count++;
	        cache[setindex][min].time=count;
	        write_prefetch(indextagn, setindexn,assoc);
	        return;
	}
		}
		
		
}
return;
}

void read_prefetchl(unsigned long  indextag,unsigned long  setindex,int assoc,unsigned long  indextagn,unsigned long  setindexn)
{	
int i,j,min;
for(i=0;i<assoc;i++)
	{
	if(cache[setindex][i].valid==0)
	{
	c_miss++;
	mem_read++;
	count++;
	cache[setindex][i].valid=1;
	cache[setindex][i].tag=indextag;
	cache[setindex][i].time=count;
	read_prefetchr(indextagn,setindexn,assoc);
	return;
	}
else{
	if(cache[setindex][i].tag==indextag)
	{
	c_hit++;
	count++;
	cache[setindex][i].time=count;
	return;
}
	if(i==(assoc-1))
	{
	c_miss++;
	mem_read++;
	min=0;
	for(j=0;j<assoc;j++)
	{
	if(cache[setindex][j].time<=cache[setindex][min].time)
	{
	min=j;
	}	
}
		cache[setindex][min].valid=1;
	        cache[setindex][min].tag=indextag;
	        count++;
	        cache[setindex][min].time=count;
	        read_prefetchr(indextagn,setindexn,assoc);
	        return;
	}
		
}
}
return;
}

//main
int main(int argc, char* argv[])
{
  int cachesize=atoi(argv[1]);
  int blocksize=atoi(argv[4]);
  int num,setnum,assoc, block, s;
  char op;
  unsigned long  addr, new_addr, set_mask, indextag, setindex,   indextagn, setindexn;

if(argv[3][0]=='f')
{

  FILE* fptr = fopen(argv[5],"r");

  if(fptr==NULL)
     {

    printf("wrong tracefile name\n");
    return 0;

     }

if(argv[2][0]=='d')
 {//direct
assoc=1;
setnum=cachesize/blocksize;
 }
else if(argv[2][5]!=':')
 {//assoc
 setnum=1;
 assoc=cachesize/blocksize;
 }
else
{
    sscanf(argv[2],"assoc:%d",&num);
    assoc=num;
    setnum=cachesize/blocksize/num;
}

//byte count
block=log(blocksize)/log(2);
s=log(setnum)/log(2);
set_mask=((1<<s)-1);

cache=cacheGenerator(setnum,assoc);

while(fscanf(fptr, "%*x: %c %lx", &op, &addr)==2)
{
setindex=(addr>>block)&set_mask;
indextag=addr>>(block+s);
//write or read
if(op=='R')
 {
  reading(indextag,setindex,assoc);
  }
else if(op=='W')
  {
  writing(indextag,setindex,assoc);
  }
}
fclose(fptr);

fptr=fopen(argv[5],"r");

if(fptr==NULL)
 {
    printf("wrong tracefile name\n");
    return 0;
  }

printf("Prefetch 0\n");
printf("Memory reads: %d\n",mem_read);
printf("Memory writes: %d\n",mem_write);
printf("Cache hits: %d\n",c_hit);
printf("Cache misses: %d\n",c_miss);
clear(setnum,assoc);

while(fscanf(fptr, "%*x: %c %lx", &op, &addr)==2)
{
setindex=(addr>>block)&set_mask;
indextag=addr>>(block+s);
new_addr=addr+blocksize;
setindexn=(new_addr>>block)&set_mask;
indextagn=new_addr>>(block+s);

if(op=='R')
 {
read_prefetch(indextag,setindex,assoc,indextagn,setindexn);
 }
else if(op=='W')
 {
write_prefetcher(indextag,setindex,assoc,indextagn,setindexn);
 }
 }
printf("Prefetch 1\n");
printf("Memory reads: %d\n",mem_read);
printf("Memory writes: %d\n",mem_write);
printf("Cache hits: %d\n",c_hit);
printf("Cache misses: %d\n",c_miss);

//lru
}
else if(argv[3][0]=='l')
 {
  FILE* fptr =fopen(argv[5],"r");
  if(fptr==NULL)
 {
  printf("cannot find tracefile with that name\n");
  return 0;
 }


if(argv[2][0]=='d')//map
{
 assoc=1;
 setnum=cachesize/blocksize;
}
else if(argv[2][5]!=':')
 {//full associative
setnum=1;
assoc=cachesize/blocksize;
 }
else
 {//associative n way
    sscanf(argv[2],"assoc:%d",&num);
    assoc=num;
    setnum=cachesize/blocksize/num;
 }

//calculate bytes
block=log(blocksize)/log(2);
s=log(setnum)/log(2);
set_mask=((1<<s)-1);
cache=cacheGenerator(setnum,assoc);

while(fscanf(fptr, "%*x: %c %lx", &op, &addr)==2)
{
setindex=(addr>>block)&set_mask;
indextag=addr>>(block+s);
 if(op=='R')
 {
 readingl(indextag,setindex,assoc);
 }
 else if(op=='W')
 {
 writingl(indextag,setindex,assoc);
 }
}
fclose(fptr);
fptr=fopen(argv[5],"r");

if(fptr==NULL)
 {
    printf("wrong trace filename\n");
    return 0;
 }
printf("Prefetch 0\n");
printf("Memory reads: %d\n",mem_read);
printf("Memory writes: %d\n",mem_write);
printf("Cache hits: %d\n",c_hit);
printf("Cache misses: %d\n",c_miss);
clear(setnum,assoc);

while(fscanf(fptr, "%*x: %c %lx", &op, &addr)==2)
{
//shifting
setindex=(addr>>block)&set_mask;
indextag=addr>>(block+s);
new_addr=addr+blocksize;
setindexn=(new_addr>>block)&set_mask;
indextagn=new_addr>>(block+s);

if(op=='R')
 {
read_prefetchl(indextag,setindex,assoc,indextagn,setindexn);
 }
else if(op=='W')
 {
write_prefetchl(indextag,setindex,assoc,indextagn,setindexn);
 }
}
 printf("Prefetch 1\n");
 printf("Memory reads: %d\n",mem_read);
 printf("Memory writes: %d\n",mem_write);
 printf("Cache hits: %d\n",c_hit);
 printf("Cache misses: %d\n",c_miss);
}
else
{
printf("values are incorrect");
}
return 0;
}
