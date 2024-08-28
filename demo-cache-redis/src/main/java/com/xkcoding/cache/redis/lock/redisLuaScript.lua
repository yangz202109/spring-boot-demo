-- 第一版
if redis.call('exists','key') == 0 then
   redis.call('hset','key','uuid:threadId',1)
   redis.call('expire','key',30)
   return 1
elseif redis.call('hexists','key','uuid:threadId') == 1 then
	redis.call('hincrby','key','uuid:threadId',1)
	redis.call('expire','key',30)
	return 1
else
	return 0
end

-- 第二版 合并相同代码,使用hincrby替代hset,精简代码
if redis.call('exists','key') == 0 or redis.call('hexists','key','uuid:threadId') == 1 then
   redis.call('hincrby','key','uuid:threadId',1)
   redis.call('expire','key',30)
   return 1
else
	return 0
end

-- 第三版 参数替换
if redis.call('exists',KEYS[1]) == 0 or redis.call('hexists',KEYS[1],ARGV[1]) == 1 then
   redis.call('hincrby',KEYS[1],ARGV[1],1)
   redis.call('expire',KEYS[1],ARGV[2])
   return 1
else
	return 0
end


-- 上锁
EVAL "if redis.call('exists',KEYS[1]) == 0 or redis.call('hexists',KEYS[1],ARGV[1]) == 1 then redis.call('hincrby',KEYS[1],ARGV[1],1) redis.call('expire',KEYS[1],ARGV[2]) return 1 else return 0 end" 1 yzredislock 111:222 30
-- 解锁
EVAL "if redis.call('hexists',KEYS[1],ARGV[1]) == 0 then return nil elseif redis.call('hincrby',KEYS[1],ARGV[1],-1) == 0 then redis.call('del',KEYS[1]) return 1 else return 0 end" 1 yzredislock 111:222

-- 第一版
if redis.call('hexists','key','uuid:threadId') == 0 then
	return nil
elseif redis.call('hincrby','key','uuid:threadId',-1) == 0 then
   redis.call('del','key')
   return 1
else
	return 0
end

-- 第二版
if redis.call('hexists',KEYS[1],ARGV[1]) == 0 then
	return nil
elseif redis.call('hincrby',KEYS[1],ARGV[1],-1) == 0 then
   redis.call('del',KEYS[1])
   return 1
else
	return 0
end

-- 续期 
if redis.call('hexists','key','uuid:threadId') == 1 then
	return redis.call('expire','key',30)
else
	return 0
end

if redis.call('hexists',KEYS[1],ARGV[1]) == 1 then
	return redis.call('expire',KEYS[1],ARGV[2])
else
	return 0
end

EVAL "if redis.call('hexists',KEYS[1],ARGV[1]) == 1 then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end" 1 yzredislock 111:222 30
