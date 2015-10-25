package lin.client.tcp;

import lin.util.ByteUtils;



public abstract class CommandPackage extends Package//ICommandPackageParser,
    {
        private int command = 1;
//        private byte _major = 0;
//        private byte _minor = 0;
//        private byte _revise = 0;

        protected CommandPackage()
        {
//            Type type = this.GetType();
//            Command c = type.GetCustomAttribute<Command>(false);
//            this._command = c.Commaand;
        }

        protected int getBodySize()
        {
            return 0;
        }

        protected void bodyWrite(byte[] bs, int offset)
        {
        }
        protected void bodyWrite(byte[] bs)
        {
        	bodyWrite(bs,0);
        }

        public int getSize()
        {
            //get { return (this.bodySize() + 11); }
        	return this.getBodySize() + 11;
        }

        @Override
        public final byte[] write()
        {
            byte[] bs = new byte[this.getSize()];
            //Utils.Write(bs, 0, 0);
            ByteUtils.writeByte(bs, this.major, 0);
            ByteUtils.writeByte(bs, this.minor, 1);
            ByteUtils.writeByte(bs, this.revise, 2);
            ByteUtils.writeInt(bs, this.command, 3);
            ByteUtils.writeInt(bs, this.getSize(), 7);
//            ByteUtils.writeInt(bs, 0, 11);
            this.bodyWrite(bs, 11);
            return bs;
        }

        public int getCommand()
        {
             return this.command;
        }

        private byte major;
        private byte minor;
        private byte revise;
        
//        public byte Major
//        {
//            get
//            {
//                return this._major;
//            }
//            internal set
//            {
//                this._major = value;
//            }
//        }
//
//        public byte Minor
//        {
//            get
//            {
//                return this._minor;
//            }
//            internal set
//            {
//                this._minor = value;
//            }
//        }
//
//        public byte Revise
//        {
//            get
//            {
//                return this._revise;
//            }
//            internal set
//            {
//                this._revise = value;
//            }
//        }

        public byte getMajor() {
			return major;
		}

		void setMajor(byte major) {
			this.major = major;
		}

		public byte getMinor() {
			return minor;
		}

		void setMinor(byte minor) {
			this.minor = minor;
		}

		public byte getRevise() {
			return revise;
		}

		void setRevise(byte revise) {
			this.revise = revise;
		}
		@Override
		public final byte getType()
        {
                return 1;
        }

        //int PackageParser.Command
        //{
        //    get { throw new NotImplementedException(); }
        //}

        
        //public abstract Package Parser2(byte[] bs);

        public abstract void parser(byte[] bs);
        //public virtual CommandPackage Parser(byte[] bs)
        //{
        //    //this.GetType()
        //    return (CommandPackage)Activator.CreateInstance(this.GetType());
        //}
    }

