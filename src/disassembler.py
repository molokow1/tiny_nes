import opcodes

class Disassembler(object):
	"""Disassemble NES rom"""
	def __init__(self, rom):
		super(Disassembler, self).__init__()
		self.rom = rom 

	def disassemble(self):
		pass
		
	def parseOpCode(self,op):
		return opcodes.opcodeDict[op]

class ROM(object):

	def __init__(self, path):
		super(ROM, self).__init__()
		self.path = path 
		self._byteList = []
		self._openROM(path)

	def _openROM(self,path):
		with open(path, 'rb') as f:
		   	while 1:
				byte_s = f.read(1)
				if not byte_s:
					break
				self._byteList.append(byte_s)

	def getByteList(self):
		return self._byteList


if __name__ == '__main__':
	rom = ROM("../rom/tetris.nes")
	disassembler = Disassembler(rom)
	for byte in rom.getByteList():
		try:
			print disassembler.parseOpCode(byte)
		except KeyError:
			pass