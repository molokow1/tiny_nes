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
		self.props = {}
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

	def parseROMInfo(self):
		#for iNES format
		validStr = ''.join(self._byteList[:4])
		self.props["valid"] = True if validStr == "NES\x1A" else False
		flag6 = self.byteToBinStr(self._byteList[6])
		flag7 = self.byteToBinStr(self._byteList[7])
		flag9 = self.byteToBinStr(self._byteList[9])
		flag10 = self.byteToBinStr(self._byteList[10])

		print flag6,flag7,flag9,flag10
		

	def byteToBinStr(self,byte):
		return '{0:08b}'.format(ord(byte))
if __name__ == '__main__':
	rom = ROM("../rom/tetris.nes")
	rom.parseROMInfo()
	print rom.props
