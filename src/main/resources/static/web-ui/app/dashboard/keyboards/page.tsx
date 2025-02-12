import { title } from "@/components/primitives";

export default function BlogPage() {
  return (
    <div className="max-w-screen-xl mx-auto px-2">
            <h1 className={title() }>Keyboards</h1>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 pt-4">

        {/* Левая колонка будет шире */}
        <div className="col-span-2 flex flex-col">

        </div>
        
        {/* Правая колонка */}
        <div className="flex flex-col">

        </div>
      </div>
    </div>
  );
}
