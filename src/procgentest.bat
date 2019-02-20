for /l %%x in (1, 1, 500) do (
	echo %%x
	java Sanctum >> testlog.txt
)
echo "Done Testing..."
